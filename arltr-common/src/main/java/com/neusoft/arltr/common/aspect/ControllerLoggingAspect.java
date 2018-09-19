/**
 * 
 */
package com.neusoft.arltr.common.aspect;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 业务Service日志处理
 * 
 *
 *
 */
@Aspect
@Component
public class ControllerLoggingAspect {

	private static Logger logger = Logger.getLogger(ControllerLoggingAspect.class);
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Before(value="execution(* com.neusoft.arltr.*.controller.*.*(..))")
	public void before(JoinPoint point) {
		
		if (!logger.isDebugEnabled()) {
			return;
		}
		
		logger.debug("URI:" + getUri(point) + " Begin");
		
		StringBuilder sb = new StringBuilder();
		sb.append(point.getTarget().getClass().getName());
		sb.append(".");
		sb.append(point.getSignature().getName());
		sb.append(":Start");
		
		if (point.getArgs() != null && point.getArgs().length > 0) {
			
			sb.append(" (");
			
			for (Object arg : point.getArgs()) {
				if (arg != null) {
					try {
						sb.append(objectMapper.writeValueAsString(arg));
					} catch (JsonProcessingException e) {
						
					}
					
				} else {
					sb.append(arg);
				}
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");
		}
		
		logger.debug(sb.toString());
	}
	
	@After(value="execution(* com.neusoft.arltr.*.controller.*.*(..))")
	public void after(JoinPoint point) {
		
		if (!logger.isDebugEnabled()) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(point.getTarget().getClass().getName());
		sb.append(".");
		sb.append(point.getSignature().getName());
		sb.append(":End");
		
		logger.debug(sb.toString());
		
		logger.debug("URI:" + getUri(point) + " Finished");
	}
	
	private String getUri(JoinPoint point) {
		
		Class<?> clazz = point.getTarget().getClass();
		String methodName = point.getSignature().getName();
		
		String uri = "";
		
		if (clazz.isAnnotationPresent(RequestMapping.class)) {
			
			RequestMapping rootMapping = clazz.getAnnotation(RequestMapping.class);
			
			uri = rootMapping.value()[0];
		}
		
		Method method = null;
		
		try {
			
			Method[] ms = clazz.getMethods();
			
			for (Method m : ms) {
				if (m.getName().equals(methodName)) {
					method = m;
					break;
				}
			}
			
			if (method.isAnnotationPresent(RequestMapping.class)) {
				RequestMapping mapping = method.getAnnotation(RequestMapping.class);
				uri += mapping.value()[0];
			} else if (method.isAnnotationPresent(GetMapping.class)) {
				GetMapping mapping = method.getAnnotation(GetMapping.class);
				uri += mapping.value()[0];
			} else if (method.isAnnotationPresent(PostMapping.class)) {
				PostMapping mapping = method.getAnnotation(PostMapping.class);
				uri += mapping.value()[0];
			}
		
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return uri;
	}
}
