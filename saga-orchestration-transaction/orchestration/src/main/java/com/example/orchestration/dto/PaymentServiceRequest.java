package com.example.orchestration.dto;

public record PaymentServiceRequest(
	String command,
	Long sagaId,
	Long orderId
) implements SagaServiceRequest {
}