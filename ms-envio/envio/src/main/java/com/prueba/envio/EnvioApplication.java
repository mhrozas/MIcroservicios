package com.prueba.envio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableFeignClients
@SpringBootApplication
@EnableEurekaServer
public class EnvioApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnvioApplication.class, args);
	}

}
