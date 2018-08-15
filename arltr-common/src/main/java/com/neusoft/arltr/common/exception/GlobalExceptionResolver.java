/**
 * 
 */
package com.neusoft.arltr.common.exception;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.arltr.common.base.RespBody;

/**
 * @author zhanghaibo
 *
 */
@Component
public class GlobalExceptionResolver extends ExceptionHandlerExceptionResolver {

	private static Logger logger = Logger.getLogger(GlobalExceptionResolver.class);
	
	ObjectMapper objMapper = new ObjectMapper();
	
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {  
    	
        if (handlerMethod == null) {  
            return null;  
        }  
  
        Method method = handlerMethod.getMethod();  
  
        if (method == null) {  
            return null;  
        }
        
        Class<?> clazz = method.getDeclaringClass();
        
        RespBody<String> respBody = new RespBody<String>();
        
        if (exception instanceof BusinessException) {
        	respBody.setCode(RespBody.BUSINESS_ERROR);
        } else {
        	respBody.setCode(RespBody.SYS_ERROR);
        	logger.error("系统发生异常", exception);
        }
        
        respBody.setMsg(exception.getMessage());
        
        if (clazz.isAnnotationPresent(RestController.class) || method.isAnnotationPresent(ResponseBody.class)) {
        	
        	ModelAndView mv = new ModelAndView();
        	
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            
            try {  
                response.getWriter().write(objMapper.writeValueAsString(respBody));  
            } catch (IOException e) {  
            	logger.debug("json转换错误");
            }

            return mv;  
        }
        
        return new ModelAndView("redirect:/error");
    }  
  
}
