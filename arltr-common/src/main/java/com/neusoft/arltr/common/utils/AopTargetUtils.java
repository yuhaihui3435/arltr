package com.neusoft.arltr.common.utils;

/**
 * 
 */
import java.lang.reflect.Field;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

public class AopTargetUtils {

	/**
	 * 获取 目标对象类名
	 * 
	 * @param target 对象
	 * 
	 * @return 目标对象类名
	 */
	public static String getTargetName(Object target) {
		
		try {
			Class<?> realTarget = getTargetClass(target);
			
			return realTarget.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return target.getClass().getName();
	}
	
	/**
	 * 获取 目标对象
	 * 
	 * @param proxy
	 *            代理对象
	 * @return
	 * @throws Exception
	 */
	public static Class<?> getTargetClass(Object proxy) throws Exception {

		if (!AopUtils.isAopProxy(proxy)) {
			return proxy.getClass();// 不是代理对象
		}

		if (AopUtils.isJdkDynamicProxy(proxy)) {
			return getJdkDynamicProxyTargetClass(proxy);
		} else { // cglib
			return getCglibProxyTargetClass(proxy);
		}
	}

	private static Class<?> getCglibProxyTargetClass(Object proxy) throws Exception {
		Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
		h.setAccessible(true);
		Object dynamicAdvisedInterceptor = h.get(proxy);

		Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
		advised.setAccessible(true);

		Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

		return target.getClass();
	}

	private static Class<?> getJdkDynamicProxyTargetClass(Object proxy) throws Exception {
		Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
		h.setAccessible(true);
		AopProxy aopProxy = (AopProxy) h.get(proxy);

		Field advised = aopProxy.getClass().getDeclaredField("advised");
		advised.setAccessible(true);

		AdvisedSupport as = (AdvisedSupport) advised.get(aopProxy);
		
		Class<?>[] ins = as.getProxiedInterfaces();
		
		for (Class<?> in : ins) {
			if (in.getPackage().getName().startsWith("com.neusoft")) {
				return in;
			}
		}
		
		Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();

		return target.getClass();
	}
}
