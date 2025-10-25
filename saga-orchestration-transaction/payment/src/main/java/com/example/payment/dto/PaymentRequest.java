package com.example.payment.dto;

public record PaymentRequest(
		String command,
		Long sagaId,
		Long orderId
) {}
