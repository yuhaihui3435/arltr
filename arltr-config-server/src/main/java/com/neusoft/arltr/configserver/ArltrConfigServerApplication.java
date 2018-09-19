/**
 * 
 */
package com.neusoft.arltr.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心
 * 
 *
 *
 */
@SpringBootApplication
@EnableConfigServer
public class ArltrConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArltrConfigServerApplication.class, args);
	}

}
