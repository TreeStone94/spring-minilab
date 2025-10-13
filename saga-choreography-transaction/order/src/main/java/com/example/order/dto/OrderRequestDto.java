package com.example.order.dto;

public record OrderRequestDto(
		Long productId,
		int quantity
) { }
