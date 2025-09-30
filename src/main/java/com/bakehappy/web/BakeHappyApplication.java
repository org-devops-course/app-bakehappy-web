package com.bakehappy.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.bakehappy.web.admin",
        "com.bakehappy.web.ecommerce"
})
public class BakeHappyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BakeHappyApplication.class, args);
	}

}
