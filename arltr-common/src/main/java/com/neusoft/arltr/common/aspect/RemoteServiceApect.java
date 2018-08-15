/**
 * 
 */
package com.neusoft.arltr.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.exception.BusinessException;

/**
 * FeignClient异常返回值处理
 * 
 * @author zhanghaibo
 *
 */
@Aspect
@Component
public class RemoteServiceApect {

	@AfterReturning(value="execution(* com.neusoft.arltr.common.service.*.*(..))", returning = "respBody")
	public void after(JoinPoint point, RespBody<?> respBody) {
		
		if (respBody.getCode() != RespBody.SUCCESS) {
			
			if (respBody.getCode() == RespBody.SYS_ERROR) {
				
				StringBuilder sb = new StringBuilder();
				sb.append("服务错误 -> ");
				sb.append(point.getTarget().getClass().getName());
				sb.append(".");
				sb.append(point.getSignature().getName());
				sb.append(" -> ");
				sb.append(respBody.getMsg());
				
				throw new RuntimeException(sb.toString());
			
			} else if (respBody.getCode() == RespBody.BUSINESS_ERROR) {
				
				throw new BusinessException(respBody.getMsg());
			}
		}
		
	}
}
