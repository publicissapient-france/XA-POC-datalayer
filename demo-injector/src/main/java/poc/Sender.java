package poc;

import io.prometheus.client.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class Sender {

    static final Counter producedMessagesSuccess = Counter.build()
            .name("produced_messages_success")
            .help("Produced messages on success.")
            .register();

    static final Counter producedMessagesError = Counter.build()
            .name("produced_messages_error")
            .help("Produced messages on error.")
            .register();

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    private final KafkaTemplate<String, OperationEvent> kafkaTemplate;

    @Autowired
    public Sender(KafkaTemplate<String, OperationEvent> kafkaTemplate) {this.kafkaTemplate = kafkaTemplate;}

    public void send(String topic, OperationEvent message) {
        // the KafkaTemplate provides asynchronous send methods returning a Future
        ListenableFuture<SendResult<String, OperationEvent>> future = kafkaTemplate.send(topic, message);

        // register a callback with the listener to receive the result of the send asynchronously
        future.addCallback(new ListenableFutureCallback<SendResult<String, OperationEvent>>() {

            @Override
            public void onSuccess(SendResult<String, OperationEvent> result) {
                LOGGER.info("sent message='{}' with offset={}", message,
                        result.getRecordMetadata().offset());
                producedMessagesSuccess.inc();
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("unable to send message='{}'", message, ex);
                producedMessagesError.inc();
            }
        });

    }
}
