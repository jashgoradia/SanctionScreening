import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;

public class ProducerClass {
    public void writeToTopic(String topic){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        Producer<String, String> producer = new KafkaProducer<>(properties);
        while(true)
        {
            System.out.println("Enter a name");
            Scanner sc = new Scanner(System.in);
            String name = sc.nextLine();

                ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, name);
                producer.send(record);
                producer.flush();
        }
    }

    public static void main(String[] args) {
        ProducerClass pc = new ProducerClass();
        pc.writeToTopic("Sanctions-Screening");
    }
}