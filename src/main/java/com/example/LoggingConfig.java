package com.example;

import java.time.Duration;
import java.util.Map;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LoggingConfig.OtlpProperties.class)
@ConditionalOnProperty(prefix = "management.otlp.logs", name = "endpoint")
public class LoggingConfig {

	@Bean
	public SdkLoggerProvider sdkLoggerProvider(Resource resource, OtlpProperties props) {
		return SdkLoggerProvider.builder().setResource(resource)
				.addLogRecordProcessor(BatchLogRecordProcessor.builder(OtlpHttpLogRecordExporter.builder()
								.setEndpoint(props.endpoint())
								.setHeaders(() -> props.headers() == null ? Map.of() : props.headers())
								.setCompression(props.compression.name().toLowerCase())
								.setTimeout(props.timeout())
								.build())
						.build())
				.build();
	}

	@Bean
	public ApplicationListener<ApplicationReadyEvent> logbackOtelAppenderInitializer(OpenTelemetry openTelemetry) {
		return event -> OpenTelemetryAppender.install(openTelemetry);
	}

	@ConfigurationProperties(prefix = "management.otlp.logs")
	public record OtlpProperties(String endpoint, @DefaultValue("10s") Duration timeout,
								 @DefaultValue("none") Compression compression, Map<String, String> headers) {

		enum Compression {
			GZIP,
			NONE

		}

	}
}
