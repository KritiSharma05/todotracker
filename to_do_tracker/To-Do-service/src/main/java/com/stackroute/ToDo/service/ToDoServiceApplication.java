package com.stackroute.ToDo.service;

import com.stackroute.ToDo.service.filter.JwtFilter;
import com.stackroute.ToDo.service.service.To_Do_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class ToDoServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(ToDoServiceApplication.class, args);
	}

	private To_Do_Service service;

	@Autowired
	public ToDoServiceApplication(To_Do_Service service) {
		this.service = service;
	}

	@Bean
	public FilterRegistrationBean filterBean() {
		//which urls to be intercepted,by using filter definition
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new JwtFilter());
		filterRegistrationBean.addUrlPatterns(
				"/to-do-service/feedback/*", "/to-do-service/getUserName", "/to-do-service/getUserDetails",
				"/to-do-service/updateProfile", "/to-do-service/addTask", "/to-do-service/updateTask",
				"/to-do-service/add-image-to-task/*", "/to-do-service/get-task-image/*",
				"/to-do-service/CompleteTask", "/to-do-service/deleteAr/*","/to-do-service/deleteDl/*",
				"/to-do-service/remove/*","/to-do-service/shift/*","/to-do-service/reShift/*",
				"/to-do-service/restore/*","/to-do-service/CompletedTask","/to-do-service/allTask",
				"/to-do-service/allArTask","/to-do-service/allPendingTask","/to-do-service/allDeletedTask",
				"/to-do-service/startDate","/to-do-service/priority","/to-do-service/endDate",
				"/to-do-service/getTask/*","/to-do-service/taskNearDue","/to-do-service/todayTask",
				"/to-do-service/update-profile-pic/*","/to-do-service/get-profile-pic");
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean(){
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:4200");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
}
