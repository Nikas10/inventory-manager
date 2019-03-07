package com.quartet.inventorydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.quartet.inventorydemo")
public class InventoryDemoApplication {

	private static final String AUTH_SERVER = "localhost:8080";

	public static void main(String[] args) {
		SpringApplication.run(InventoryDemoApplication.class, args);
	}
}
