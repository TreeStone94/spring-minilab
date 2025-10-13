package com.example.payment.dto;

import com.example.payment.enums.OrderStatus;
import lombok.Data;

@Data
public class PaymentEvent {
	private Long overId;
	private OrderStatus status;
}
