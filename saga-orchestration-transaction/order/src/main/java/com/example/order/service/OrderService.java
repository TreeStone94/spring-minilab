package com.example.order.service;

import com.example.order.dto.OrderReply;
import com.example.order.dto.ServiceRequest;
import com.example.order.entity.Order;
import com.example.order.kafka.SagaProducer;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final SagaProducer sagaProducer;

	@Transactional
	public void createOrder(ServiceRequest request) {
		Order order = new Order();
		order.setProductId(request.productId());
		order.setQuantity(request.quantity());
		order.setStatus("CREATED");
		orderRepository.save(order);

		OrderReply orderReply = new OrderReply(request.sagaId(), "ORDER_CREATED");
		sagaProducer.sendOrderReply(orderReply);

	}

	@Transactional
	public void cancelOrder(ServiceRequest request) {
		Order order = orderRepository.findById(request.orderId()).orElse(null);
		if (order != null) {
			order.setStatus("CANCELLED");
			orderRepository.save(order);
		}
	}
}
