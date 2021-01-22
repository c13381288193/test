package com.redoop.monitor.cdh.yarn;

import com.redoop.monitor.cdh.kudu.KuduMonitorApplication;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
public class YarnMonitorAppplication {
    public static void main(String[] args) {
        SpringApplication.run(YarnMonitorAppplication.class, args);
    }
}
