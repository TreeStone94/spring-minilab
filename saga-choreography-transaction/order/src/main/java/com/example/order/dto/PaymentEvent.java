package com.example.order.dto;

import com.example.order.enums.OrderStatus;

public record PaymentEvent(
		Long overId,
		OrderStatus status
) {
}
