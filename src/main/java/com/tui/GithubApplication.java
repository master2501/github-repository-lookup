package com.tui;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.tui.handlers.CustomErrorAttributes;

@ComponentScan(basePackages = "com.tui")
@SpringBootApplication
public class GithubApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public CustomErrorAttributes errorAttributes() {
		return new CustomErrorAttributes();
	}

	public static void main(String[] args) {
		SpringApplication.run(GithubApplication.class, args);
	}
}