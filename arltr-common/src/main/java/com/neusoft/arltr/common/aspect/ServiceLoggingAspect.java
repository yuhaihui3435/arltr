/**
 * 
 */
package com.neusoft.arltr.common.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 业务Service日志处理
 * 
 * @author zhanghaibo
 *
 */
@Aspect
@Component
public class ServiceLoggingAspect {

	private static Logger logger = Logger.getLogger(ServiceLoggingAspect.class);
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Before(value="execution(* com.neusoft.arltr.*.service.*.*(..))")
	public void before(JoinPoint point) {
		
		if (!logger.isDebugEnabled()) {
			return;
		}
		
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
	
	@After(value="execution(* com.neusoft.arltr.*.service.*.*(..))")
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
	}
}
