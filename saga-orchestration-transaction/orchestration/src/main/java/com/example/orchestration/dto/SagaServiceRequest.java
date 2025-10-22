package com.example.orchestration.dto;

public interface SagaServiceRequest {
	String command();
	Long sagaId();
}
