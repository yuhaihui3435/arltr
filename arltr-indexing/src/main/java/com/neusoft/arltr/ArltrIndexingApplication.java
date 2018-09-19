/**
 * 
 */
package com.neusoft.arltr;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import com.neusoft.arltr.common.utils.HttpSolrClient;
import com.neusoft.arltr.indexing.utils.SpringContextUtil;

/**
 * 搜索服务启动程序
 * 
 *
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
public class ArltrIndexingApplication {

	@Value("${spring.data.solr.host}")
	private  String solrHost;
	
    @Resource private Environment environment;

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient(solrHost);
    }
    
	public static void main(String[] args) {
		System.setProperty ("jsse.enableSNIExtension", "false");
		//此处添加applicationcontext获取，因为在任务调用的时候上下文的内容不是一个主体，所以在启动时候注入applicationContext
		ApplicationContext applicationContext = SpringApplication.run(ArltrIndexingApplication.class, args);
		SpringContextUtil.setApplicationContext(applicationContext);
		//SpringApplication.run(ArltrIndexingApplication.class, args);
	}

}
