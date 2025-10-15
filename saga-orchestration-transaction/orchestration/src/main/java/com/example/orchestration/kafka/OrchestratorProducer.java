package com.example.orchestration.kafka;

import com.example.orchestration.dto.ServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrchestratorProducer {
	private final KafkaTemplate<String, ServiceRequest> kafkaTemplate;

	public void sendToOrderService(ServiceRequest request) {
		this.kafkaTemplate.send("order-requests", request);
	}

	public void sendToPaymentService(ServiceRequest request) {
		this.kafkaTemplate.send("payment-requests", request);
	}

	public void sendToStockService(ServiceRequest request) {
		this.kafkaTemplate.send("stock-requests", request);
	}
}
