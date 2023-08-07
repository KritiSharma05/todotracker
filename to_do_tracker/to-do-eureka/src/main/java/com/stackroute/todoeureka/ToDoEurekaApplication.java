package com.stackroute.todoeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@SpringBootApplication
@EnableEurekaServer

public class ToDoEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoEurekaApplication.class, args);
	}

}
