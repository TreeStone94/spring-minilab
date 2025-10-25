package com.example.order.dto;

public record OrderRequest(
		String command,
		Long sagaId,
		Long productId,
		Integer quantity
) {
}
