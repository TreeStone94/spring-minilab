package com.example.order.kafka;

import com.example.order.dto.OrderReply;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {
	private final KafkaTemplate<String, OrderReply> kafkaTemplate;
	public void sendOrderReply(OrderReply reply) {
		this.kafkaTemplate.send("order-replies", reply);
	}
}
