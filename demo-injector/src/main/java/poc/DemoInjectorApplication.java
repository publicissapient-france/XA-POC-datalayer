package poc;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@SpringBootApplication
@EnableScheduling
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
public class DemoInjectorApplication {
    final String[] users = {
            "Neil Armstrong",
            "Marco Polo",
            "Vasco da Gama",
            "Christopher Columbus",
            "Ferdinand Magellan",
            "Hernan Cortes",
            "Lewis and Clark",
            "John Smith",
            "Amerigo Vespucci",
            "Francisco Pizarro",
            "James Cook",
            "John Cabot",
            "Jacques Cartier",
            "Daniel Boone",
            "Sacagawea",
            "Henry Hudson",
            "Jacques Cousteau",
            "Francis Drake",
            "Samuel de Champlain",
            "Zheng He",
            "Juan Ponce de Leon",
            "Hernando de Soto"

    };
    @Value("${app.topic-test}")
    private String topic;

    public static void main(String[] args) {
        SpringApplication.run(DemoInjectorApplication.class, args);
    }

    @Autowired
    private Sender sender;

    @Scheduled(fixedRateString = "500")
    public void inject() {
        OperationEvent operationEvent = new OperationEvent(randomClient(), randomAmount(), "some message");
        sender.send(topic, operationEvent);
    }

    String randomClient() {
        return users[randomClientId()];
    }

    private int randomClientId() {
        return (int) (Math.random() * users.length);
    }

    Double randomAmount() {
        return 10. + (int) (Math.random() * 10000)/100;
    }
}
