package com.kafka.kafk;

import java.util.Collections;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kafka.mq.service.MessageService;


@Component
public class TxConsumer {
	
	@Value("${spring.batchproducer.groupId}")
	private String groupid;
	
	@Value("${spring.batchproducer.topicName}")
	private String topic;

	@Value("${spring.batchproducer.bootstrapServer}")
	private String bootstrapServer;

	@Value("${spring.batchproducer.autoffset}")
	private String autoofset;
	
	@Value("${spring.batchproducer.isolationlevelconfig}")
	private String isolationlevelconfig;
	
	@Autowired
	MessageService service;

	private KafkaConsumer<String, String> consumer;

	
	public void receiveMessage() {
	
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(1000);
			for (ConsumerRecord<String, String> record : records) {
				System.out.printf("Received Message topic =%s, partition =%s, offset = %d, key = %s, value = %s\n",
						record.topic(), record.partition(), record.offset(), record.key(), record.value());
				System.out.println("Message consumed from Kafka Broker: "+record.value());
				service.publish(record.value());
			}
			consumer.commitSync();
		}

	}
	
	/**
	 * This method is used to return Consumer instance
	 */
	@PostConstruct
	public void createConsumer() {
		Properties consumerConfig = new Properties();
		consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupid);
		consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoofset);
		consumerConfig.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, isolationlevelconfig);
		consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumer = new KafkaConsumer<>(consumerConfig);
		consumer.subscribe(Collections.singletonList(topic));
	}
}