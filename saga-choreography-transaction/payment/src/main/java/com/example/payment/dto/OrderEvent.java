package com.example.payment.dto;


public record OrderEvent(
		Long orderId, Long productId, int quantity
) {
}
