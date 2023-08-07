package com.stackroute.to_do_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient

public class ToDoGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoGatewayApplication.class, args);
	}

}
