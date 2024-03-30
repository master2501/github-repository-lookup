package com.tui.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "config")
public class RepositoryConfig {
	private String baseUrl;
	private String users;
	private String repositories;
	private String branches;
	
	public String getUsersUrl() {
		return baseUrl + users;
	}
	
	public String getRepositoriesUrl() {
		return baseUrl + repositories;
	}
	
	public String getBranchesUrl() {
		return baseUrl + branches;
	}
}
