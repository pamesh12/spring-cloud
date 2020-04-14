package com.pamesh.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

import com.pamesh.rest.util.ApplicationProperties;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "rest")
	@RefreshScope
	public ApplicationProperties applicationProperties() {
		return new ApplicationProperties();
	}
}
