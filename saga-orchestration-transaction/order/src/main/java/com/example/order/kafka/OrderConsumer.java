package com.example.order.kafka;

import com.example.order.dto.OrderRequest;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConsumer {
	private final OrderService orderService;

	@KafkaListener(topics = "order-requests")
	public void handleOrderRequest(OrderRequest request) {
		if ("CREATE_ORDER".equals(request.command())) {
			orderService.createOrder(request);
		} else if("CANCEL_ORDER".equals(request.command())) {
			orderService.cancelOrder(request);
		}
	}
}
