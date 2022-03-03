package com.poc.es;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticSearchDemoApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ElasticSearchDemoApplication.class, args);
	}
}
