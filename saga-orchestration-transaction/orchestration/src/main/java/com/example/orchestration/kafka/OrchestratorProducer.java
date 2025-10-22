package com.example.orchestration.kafka;

import com.example.orchestration.dto.OrderServiceRequest;
import com.example.orchestration.dto.PaymentServiceRequest;
import com.example.orchestration.dto.StockServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrchestratorProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void sendToOrderService(OrderServiceRequest request) {
		this.kafkaTemplate.send("order-requests", request);
	}

	public void sendToPaymentService(PaymentServiceRequest request) {
		this.kafkaTemplate.send("payment-requests", request);
	}

	public void sendToStockService(StockServiceRequest request) {
		this.kafkaTemplate.send("stock-requests", request);
	}
}
