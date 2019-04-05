package com.quartet.inventorydemo;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.quartet.inventorydemo")
@EnableProcessApplication
public class InventoryDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryDemoApplication.class, args);
	}
}
