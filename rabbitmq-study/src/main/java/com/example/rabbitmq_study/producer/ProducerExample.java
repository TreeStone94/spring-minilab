package com.example.rabbitmq_study.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerExample {
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setVirtualHost("duzon");
        factory.setPort(5672);
        factory.setUsername("pertest");
        factory.setPassword("pertest");
		factory.setHost("172.16.113.72");

		Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
		String QUEUE_NAME = "tps-test";

		channel.queueDeclare(QUEUE_NAME, false, false, true, null);

		for (int i = 1; i <= 100; i++) {
			String message = "message " + i;
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
			if (i % 100 == 0) {
				System.out.println("Sent " + i + " messages...");
			}
		}
	}
}
