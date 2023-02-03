package com.example.uberbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class UberBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UberBackendApplication.class, args);
	}
}
