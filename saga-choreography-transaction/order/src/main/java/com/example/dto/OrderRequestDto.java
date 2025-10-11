package com.example.order;

public record OrderRequestDto(
		Long productId,
		int quantity
) { }
