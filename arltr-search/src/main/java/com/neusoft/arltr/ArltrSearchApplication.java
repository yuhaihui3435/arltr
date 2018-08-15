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
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.neusoft.arltr.common.utils.HttpSolrClient;

/**
 * 搜索服务启动程序
 * 
 * @author zhanghaibo
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ArltrSearchApplication {

	@Value("${spring.data.solr.host}")
	private  String solrHost;
	
    @Resource private Environment environment;

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient(solrHost);
    }
    
	public static void main(String[] args) {
		SpringApplication.run(ArltrSearchApplication.class, args);
	}

}
