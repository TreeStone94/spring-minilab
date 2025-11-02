package com.example.orchestration.dto;

public record OrderRequest(
		Long productId,
		Integer quantity
) {
}
