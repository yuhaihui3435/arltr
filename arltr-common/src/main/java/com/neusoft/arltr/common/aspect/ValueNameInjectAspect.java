package com.neusoft.arltr.common.aspect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neusoft.arltr.common.entity.user.Enumeration;
import com.neusoft.arltr.common.entity.user.EnumerationValueName;
import com.neusoft.arltr.common.service.SysService;


@Aspect
@Component
public class ValueNameInjectAspect {
@Autowired
SysService sysService;
@SuppressWarnings("unchecked")
@AfterReturning(value="execution(* com.neusoft.arltr.*.repository.*.*(..))",returning="result")
public void afterReturning(Object result){
	if(result==null||result.getClass().isPrimitive()){
		return;
	}
	if (Iterable.class.isAssignableFrom(result.getClass())) {
		
		Iterable<Object> it = (Iterable<Object>)result;
		
		for (Object entity : it) {
			injectValueNameToEntity(entity);
		}
	} else {
		injectValueNameToEntity(result);
	}
}
public void injectValueNameToEntity(Object entity){
	if(entity==null||entity.getClass().isPrimitive()){
		return;
	}
	Class<?> clazz = entity.getClass();
	while (clazz != null && !clazz.getName().equals("java.lang.Object")) {
		//获取类的字段成员变量
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			//判断元素是否包含指定类型的注解
			if (field.isAnnotationPresent(EnumerationValueName.class)) {				
				// 枚举值名称注入
				injectEnumValueName(clazz, entity, field);
			} 					
		}
		//获取超类
		clazz = clazz.getSuperclass();
	}
}
private void injectEnumValueName(Class<?> clazz, Object entity, Field field) {
	//获取字段中包含EnumerationValueName的注解
	EnumerationValueName evn = field.getAnnotation(EnumerationValueName.class);	
	String type = evn.type();
	String valueFieldName = evn.valueFieldName();	
	if (type == null || type.isEmpty()) {
		return;
	}	
	if (valueFieldName == null || valueFieldName.isEmpty()) {
		// 默认规则，去掉枚举值名称字段的后缀作为枚举值字段名
		if (field.getName().endsWith("Name")) {
			valueFieldName = field.getName().substring(0, field.getName().length() - "Name".length());
		} else {
			return;
		}
	}
	
	try {
		// 获取枚举值字段的值
		String value = getFieldValueAsString(clazz, entity, valueFieldName);
		
		// 根据类型和值获得枚举名称
		//=sysService.getListByType(type).getBody();
//		List<Enumeration> elist=enumerationRepository.getEnumerationList(type);
		List<Enumeration> elist=sysService.getListByType(type).getBody();
		Enumeration enumeration=null;
		for(Enumeration e:elist){
			if(e.getValue().equals(value)){
				enumeration=e;
			}
		}
		//Enumeration enumeration = enumerationService.getEnumeration(type, value);
		String name = enumeration == null ? "" : enumeration.getValueName();
		
		// 设置枚举值名称字段的值
		setFieldValue(clazz, entity, field, name);
		
	} catch (Exception e) {
		//logger.error("注入枚举值名称错误", e);
		e.printStackTrace();
	}
}
private String getFieldValueAsString(Class<?> clazz, Object object, String fieldName) throws Exception{	
	Field valueField = clazz.getDeclaredField(fieldName);
	PropertyDescriptor valueProperty = new PropertyDescriptor(valueField.getName(), clazz);
	String value = String.valueOf(valueProperty.getReadMethod().invoke(object));
	return value;
}
private void setFieldValue(Class<?> clazz, Object object, Field field, String value) throws Exception {
	PropertyDescriptor nameProperty = new PropertyDescriptor(field.getName(), clazz);
	nameProperty.getWriteMethod().invoke(object, value);
}
}
