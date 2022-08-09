package com.generoso.ft.sd.client;

import org.hawkular.agent.prometheus.PrometheusMetricsProcessor;
import org.hawkular.agent.prometheus.text.TextPrometheusMetricsProcessor;
import org.hawkular.agent.prometheus.types.MetricFamily;
import org.hawkular.agent.prometheus.walkers.CollectorPrometheusMetricsWalker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class MetricsClientRequest {

    @Autowired
    private PrivateMetricsRequestTemplate privateMetricsRequestTemplate;

    @Autowired
    private Client client;

    public List<MetricFamily> collectMetrics() {
        try {
            HttpResponse<String> response = client.execute(privateMetricsRequestTemplate);
            return parsePrometheusMetrics(response.body());
        } catch (IOException e) {
            throw new RuntimeException("Unable to gather metrics", e);
        }
    }

    private List<MetricFamily> parsePrometheusMetrics(String metricEndpointResponseBody) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(metricEndpointResponseBody.getBytes(StandardCharsets.UTF_8));

        CollectorPrometheusMetricsWalker collector = new CollectorPrometheusMetricsWalker();
        PrometheusMetricsProcessor<?> processor = new TextPrometheusMetricsProcessor(inputStream, collector);
        processor.walk();

        return collector.getAllMetricFamilies();
    }
}
