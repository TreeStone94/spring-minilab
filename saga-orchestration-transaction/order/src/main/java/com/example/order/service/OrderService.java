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
		order.setOrderId(request.sagaId());
		order.setProductId(request.productId());
		order.setQuantity(request.quantity());
		order.setStatus(Order.OrderStatus.CREATED);
		orderRepository.save(order);

		OrderReply orderReply = new OrderReply(request.sagaId(), "ORDER_CREATED");
		orderProducer.sendOrderReply(orderReply);

	}

	@Transactional
	public void cancelOrder(OrderRequest request) {
		Order order = orderRepository.findById(request.sagaId()).orElse(null);
		if (order != null) {
			order.setStatus(Order.OrderStatus.CANCELLED);
			orderRepository.save(order);
		}
	}
}
