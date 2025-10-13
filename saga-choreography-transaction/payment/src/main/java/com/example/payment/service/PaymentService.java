package com.example.payment.service;

import com.example.payment.dto.OrderEvent;
import com.example.payment.enums.OrderStatus;
import com.example.payment.dto.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Value("${app.kafka.topic.payment-events}")
	private String paymentEventsTopic;
	public void processPayment(OrderEvent orderEvent) {
		PaymentEvent paymentEvent = new PaymentEvent();
		paymentEvent.setOverId(orderEvent.orderId());

		if(orderEvent.quantity() < 10) {
			log.error("Payment successful for order ID: {}", orderEvent.orderId());
			paymentEvent.setStatus(OrderStatus.COMPLETED);
		} else {
			log.error("Payment cancelled for order ID: {}", orderEvent.orderId());
			paymentEvent.setStatus(OrderStatus.CANCELLED);
		}
		kafkaTemplate.send(paymentEventsTopic, paymentEvent);
	}
}
