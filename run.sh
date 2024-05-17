#!/bin/bash
set -e
DIR=$(dirname $0)
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-javaagent:${DIR}/opentelemetry-javaagent.jar \
                                                         -Dotel.experimental.resource.disabled.keys=process.command_args,process.executable.path,process.runtime.name,process.runtime.version \
                                                         -Dotel.exporter.otlp.compression=gzip \
                                                         -Dotel.exporter.otlp.endpoint=http://localhost:5080/api/default/ \
                                                         -Dotel.exporter.otlp.headers='Authorization=Basic cm9vdEBleGFtcGxlLmNvbTpzdlRkaTFtQjltNk1vRHd5' \
                                                         -Dotel.instrumentation.logback-appender.experimental-log-attributes=true \
                                                         -Dotel.instrumentation.logback-appender.experimental.capture-key-value-pair-attributes=true \
                                                         -Dotel.logs.exporter=otlp \
                                                         -Dotel.metrics.exporter=none \
                                                         -Dotel.traces.exporter=otlp \
                                                         -Dspring.output.ansi.enabled=always"