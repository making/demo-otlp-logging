package com.example;

import java.util.Map;

import am.ik.spring.http.client.RetryableClientHttpRequestInterceptor;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class HelloController {
	private final RestClient restClient;

	private final Logger log = LoggerFactory.getLogger(HelloController.class);

	public HelloController(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder
				.baseUrl("https://httpbin.org")
				.requestInterceptor(new RetryableClientHttpRequestInterceptor(new FixedBackOff(1000, 2)))
				.build();
	}

	@GetMapping(path = "/")
	public JsonNode hello() {
		return this.restClient.post()
				.uri("/post")
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of("message", "Hello World!"))
				.retrieve()
				.body(JsonNode.class)
				.get("json");
	}
}
