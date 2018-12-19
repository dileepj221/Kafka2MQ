package com.kafka.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.kafka.kafk.TxConsumer;


@SpringBootApplication
@ComponentScan(value="com.kafka.*")
public class Kafka2Mqpoc5Application implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(Kafka2Mqpoc5Application.class, args);
	}

	@Autowired
	TxConsumer txConsumer;
	
	@Override
	public void run(String... args) throws Exception {
		txConsumer.receiveMessage();
	}
}
