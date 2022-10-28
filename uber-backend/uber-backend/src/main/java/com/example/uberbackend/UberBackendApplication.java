package com.example.uberbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class UberBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UberBackendApplication.class, args);
	}

}
