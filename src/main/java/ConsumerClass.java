import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;

public class ConsumerClass {
    public void readFromTopic(String topic) throws IOException {
        SanctionsListScreening sls = new SanctionsListScreening(true,"categorisedSancList.csv");
        Scanner sc = new Scanner(System.in);

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "my-first-consumer-group");

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        Consumer<String, String> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singleton(topic));
        boolean flag = true;

        while (flag) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    sls.readSanctionsList();
                    sls.verifySanctionsList(record.value());
                }
                consumer.commitAsync();
            }
        consumer.close();
    }

    public static void main(String[] args) throws IOException {
        ConsumerClass cc = new ConsumerClass();
        cc.readFromTopic("Sanctions-Screening");
    }
}
