package com.example.rabbitmq_study.consumer;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerExample {

	private static final int MESSAGE_COUNT = 1000;

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

		AtomicInteger count = new AtomicInteger(0);
        long[] startTime = new long[1];

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			int current = count.incrementAndGet();

			if (current == 1) {
				startTime[0] = System.nanoTime(); // 첫 메시지 수신 시간 기록
			}

			String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
			System.out.println(" [x] Received: '" + message + "'");
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				throw new RuntimeException(e);
//			}


			if (current == MESSAGE_COUNT) {
				long endTime = System.nanoTime();
				long elapsedMs = (endTime - startTime[0]) / 1_000_000;
				System.out.printf("[x] Received %d messages in %d ms%n", MESSAGE_COUNT, elapsedMs);

				// Optional: 프로세스 종료
				System.exit(0);
			}
		};

		channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

	}


}
