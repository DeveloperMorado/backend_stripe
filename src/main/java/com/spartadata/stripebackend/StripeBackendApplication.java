package com.spartadata.stripebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Arrays;


@SpringBootApplication
public class StripeBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(StripeBackendApplication.class, args);

//		ApplicationContext ctx = SpringApplication.run(StripeBackendApplication.class, args);
//
//
//
//
//		// Add this debug code temporarily
//		System.out.println("Let's inspect the beans provided by Spring Boot:");
//		String[] beanNames = ctx.getBeanDefinitionNames();
//		Arrays.sort(beanNames);
//		for (String beanName : beanNames) {
//			System.out.println(beanName);
//		}

	}

}
