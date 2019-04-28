package com.quartet.inventorydemo;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableAutoConfiguration

@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing

@ComponentScan("com.quartet.inventorydemo")
@EnableProcessApplication
public class InventoryDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryDemoApplication.class, args);
	}

	//TODO make these annotations work under @Configuration class
	//@EnableTransactionManagement
	//@EnableJpaRepositories
	//@EnableJpaAuditing
}
