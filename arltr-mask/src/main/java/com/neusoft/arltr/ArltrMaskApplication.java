/**
 * 
 */
package com.neusoft.arltr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 搜索服务启动程序
 * 
 * @author zhanghaibo
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ArltrMaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArltrMaskApplication.class, args);
	}

}
