package com.example.order.service;

import com.example.order.dto.OrderReply;
import com.example.order.dto.OrderRequest;
import com.example.order.entity.Order;
import com.example.order.kafka.OrderProducer;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderProducer orderProducer;

	@Transactional
	public void createOrder(OrderRequest request) {
		Order order = new Order();
		order.setSagaId(request.sagaId());
		order.setProductId(request.productId());
		order.setQuantity(request.quantity());
		orderRepository.save(order);

		// 실패 처리
		String command = "ORDER_CREATED";
		if (request.productId() % 2 == 0) {
			order.setStatus(Order.OrderStatus.FAILED);
			command = "ORDER_FAILED";
		} else {
			order.setStatus(Order.OrderStatus.CREATED);
		}

		OrderReply orderReply = new OrderReply(request.sagaId(), command);
		orderProducer.sendOrderReply(orderReply);
	}

	@Transactional
	public void cancelOrder(OrderRequest request) {
		Order order = orderRepository.findBySagaId(request.sagaId()).orElse(null);
		if (order != null) {
			order.setStatus(Order.OrderStatus.CANCELLED);
			orderRepository.save(order);
		}
	}
}
