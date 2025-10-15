package com.example.orchestration.dto;

public record PaymentReply(
		Long sagaId,
		String status
) {
}
