/**
 * 
 */
package com.neusoft.arltr.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 服务注册中心
 * 
 * @author zhanghaibo
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class ArltrEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArltrEurekaServerApplication.class, args);
	}

}
