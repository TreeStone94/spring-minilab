package com.example.dto;

import com.example.order.entity.OrderStatus;

public record PaymentEvent(
		Long overId,
		OrderStatus status
) {
}
