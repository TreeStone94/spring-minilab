package com.example.orchestration.dto;

public record OrderServiceRequest(
	String command,
	Long sagaId,
	Long productId,
	int quantity
) implements SagaServiceRequest {
}