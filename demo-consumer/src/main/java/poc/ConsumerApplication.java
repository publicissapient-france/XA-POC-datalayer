package poc;

import io.prometheus.client.Counter;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.web.EnablePrometheusTiming;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableKafka
@EnablePrometheusEndpoint
@EnablePrometheusTiming
public class ConsumerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerApplication.class);

    static final Counter messagesSuccess = Counter.build()
            .name("consumed_messages_success")
            .help("Consumed messages on success.")
            .register();

    static final Counter messagesError = Counter.build()
            .name("consumed_messages_error")
            .help("Consumed messages on error.")
            .register();

    private final AccountRepository accountRepository;
    private final KafkaProperties kafkaProperties;

    @Autowired
    public ConsumerApplication(AccountRepository accountRepository, KafkaProperties kafkaProperties) {
        this.accountRepository = accountRepository;
        this.kafkaProperties = kafkaProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    public ConsumerFactory<String, OperationEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.kafkaProperties.getConsumer().getGroupId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
        JsonDeserializer<OperationEvent> jsonDeserializer = new JsonDeserializer<>(OperationEvent.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OperationEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OperationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @KafkaListener(topics = "${app.topic-test}")
    public void receive(OperationEvent operation) {
        LOGGER.info("received account={} message='{}'", operation.getAccount(), operation.getComment());
        Account account = accountRepository.findOne(operation.getAccount());
        if (account == null) {
            account = new Account();
            account.setId(operation.getAccount());
        }
        account.getOperations().add(new Operation(operation.getAmount(), operation.getComment()));
        accountRepository.save(account);
        messagesSuccess.inc();
    }

}
