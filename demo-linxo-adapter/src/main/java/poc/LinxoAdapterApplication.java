package poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@SpringBootApplication
@EnableKafka
@EnableScheduling
public class LinxoAdapterApplication {

    @Value("${export-dir}")
    private String exportDir;

    private static final Logger LOGGER = LoggerFactory.getLogger(LinxoAdapterApplication.class);

    private PrintWriter writer;

    public static void main(String[] args) {
        SpringApplication.run(LinxoAdapterApplication.class, args);
    }

    public LinxoAdapterApplication() {
    }

    @PostConstruct
    public void init() {
        rotateWriter();
    }

    synchronized private void rotateWriter() {
        String baseFilename = exportDir + File.separator + "linxo";
        String ext = ".csv";
        if (this.writer != null) {
            String newName = String.format("%s-%d%s", baseFilename, System.currentTimeMillis(), ext);
            this.writer.close();
            new File(baseFilename + ext).renameTo(new File(newName));
            LOGGER.info("File rotated to {}", newName);
        }

        try {
            this.writer = new PrintWriter(baseFilename + ext, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${app.topic-test}")
    public void receive(OperationEvent operation) {
        LOGGER.info("received account={} message='{}'", operation.getAccount(), operation.getComment());
        this.writer.write(operation.toCSV() + "\n");

    }

    @Scheduled(fixedRate = 10000L)
    public void rotateWriterScheduler() {
        rotateWriter();
    }


}
