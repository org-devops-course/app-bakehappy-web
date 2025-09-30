package com.example.Proyecto_DAW;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.example.Proyecto_DAW.admin",
		"com.example.Proyecto_DAW.ecommerce",
})

public class ProyectoDawApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoDawApplication.class, args);
	}

}
