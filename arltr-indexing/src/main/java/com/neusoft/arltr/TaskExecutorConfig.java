package com.neusoft.arltr;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class TaskExecutorConfig implements AsyncConfigurer{

	private static final Logger logger = Logger.getLogger(TaskExecutorConfig.class.getName());
	
	@Value("${arltr.task.corePoolSize}")
	private int corePoolSize;
	
	@Value("${arltr.task.maxPoolSize}")
	private int maxPoolSize;
	
	@Value("${arltr.task.queueCapacity}")
	private int queueCapacity;
	
    @Override
    public Executor getAsyncExecutor() {
         ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            taskExecutor.setCorePoolSize(corePoolSize);
            taskExecutor.setMaxPoolSize(maxPoolSize);
            taskExecutor.setQueueCapacity(queueCapacity);
            taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            taskExecutor.initialize();
            return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {

        	ObjectMapper objectMapper = new ObjectMapper();
        	
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				try {
					logger.error("线程出现异常：" + ex.getMessage() + "[" + method.getName() + "]" + "[" + objectMapper.writeValueAsString(params) + "]");
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
        	
        };
    }
}