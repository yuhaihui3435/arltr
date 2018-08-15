/**
 * 
 */
package com.neusoft.arltr;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.neusoft.arltr.interceptor.AuthenticatingInterceptor;

/**
 * Web应用配置类
 * 
 * @author zhanghaibo
 *
 */
@Configuration
public class ArltrWebConfig extends WebMvcConfigurerAdapter {

	@Bean
	AuthenticatingInterceptor authenticatingInterceptor() {
		return new AuthenticatingInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(authenticatingInterceptor()).addPathPatterns("/**").excludePathPatterns("/user/login/**",
				"/logon", "/error","/api/**");

		super.addInterceptors(registry);
	}
	
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

	   return (container -> {
	        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error.html");
	        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error.html");
	        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error.html");

	        container.addErrorPages(error401Page, error404Page, error500Page);
	   });
	}
}
