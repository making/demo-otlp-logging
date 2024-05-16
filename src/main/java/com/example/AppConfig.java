package com.example;

import am.ik.accesslogger.AccessLogger;
import am.ik.accesslogger.AccessLoggerBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	@Bean
	public AccessLogger accessLogger() {
		return AccessLoggerBuilder.accessLogger()
				.addKeyValues(true)
				.build();
	}

}