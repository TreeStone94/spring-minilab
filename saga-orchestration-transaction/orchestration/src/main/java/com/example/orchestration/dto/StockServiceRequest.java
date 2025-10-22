package com.example.orchestration.dto;

public record StockServiceRequest(
	String command,
	Long sagaId,
	Long productId,
	int quantity
) implements SagaServiceRequest {
}