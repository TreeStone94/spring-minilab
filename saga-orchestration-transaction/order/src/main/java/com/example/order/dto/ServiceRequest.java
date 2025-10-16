package com.example.order.dto;

public record ServiceRequest(
		String command,
		Long sagaId,
		Long orderId,
		Long productId,
		Integer quantity
) {
}
