package com.example.orchestration.dto;

public record ServiceRequest(
		String command,
		Long sagaId,
		Long productId,
		int quantity

) {
}
