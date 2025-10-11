package com.example.dto;

public record OrderRequestDto(
		Long productId,
		int quantity
) { }
