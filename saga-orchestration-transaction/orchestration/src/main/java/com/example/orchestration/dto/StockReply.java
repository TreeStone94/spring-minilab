package com.example.orchestration.dto;

public record StockReply(
		Long sagaId,
		String status
) {
}
