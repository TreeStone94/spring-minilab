package com.example.stock.dto;

public record StockRequest(
		String command,
		Long sagaId,
		Long productId,
		Integer quantity) {
}
