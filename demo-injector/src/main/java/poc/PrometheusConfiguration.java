package poc;

import io.prometheus.client.CollectorRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(CollectorRegistry.class)
public class PrometheusConfiguration {

    //@Bean
    //@ConditionalOnMissingBean
    //CollectorRegistry metricRegistry() {
    //    return CollectorRegistry.defaultRegistry;
    //}

    //@Bean
    //ServletRegistrationBean registerPrometheusExporterServlet(CollectorRegistry metricRegistry) {
    //    return new ServletRegistrationBean(new MetricsServlet(metricRegistry), "/prometheus");
    //}

}
