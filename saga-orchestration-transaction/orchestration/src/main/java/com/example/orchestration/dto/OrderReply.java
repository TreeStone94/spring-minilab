package com.example.orchestration.dto;

public record OrderReply(
		Long sagaId,
		String status
) {
}
