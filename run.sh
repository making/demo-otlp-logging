#!/bin/bash
set -e
DIR=$(dirname $0)
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-javaagent:${DIR}/opentelemetry-javaagent.jar \
                                                         -Dotel.experimental.resource.disabled.keys=process.command_args,process.executable.path,process.runtime.name,process.runtime.version,process.pid,process.runtime.description,os.type,os.description,host.arch,host.name,service.instance.id,service.version \
                                                         -Dotel.exporter.otlp.compression=gzip \
                                                         -Dotel.exporter.otlp.endpoint=http://localhost:4318 \
                                                         -Dotel.exporter.otlp.headers='Authorization=Bearer changeme' \
                                                         -Dotel.instrumentation.logback-appender.experimental-log-attributes=true \
                                                         -Dotel.instrumentation.logback-appender.experimental.capture-key-value-pair-attributes=true \
                                                         -Dotel.logs.exporter=otlp \
                                                         -Dotel.metrics.exporter=none \
                                                         -Dotel.traces.exporter=none \
                                                         -Dspring.output.ansi.enabled=always"