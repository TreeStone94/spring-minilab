package com.example.payment.dto;

public record OrderCreatedEvent (
    Long orderId,
	Long sagaId,
    double amount
) {}
