package com.kafka.mq.service;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageService {
	
	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	Queue queue;

	public String publish(final String message) {
		jmsTemplate.convertAndSend(queue, message);
		System.out.println("Kafka consumed Message sending to MQ: " + message);
		return "Message sent successfully";
	}
}
