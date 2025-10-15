package com.example.orchestration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "saga_state")
@Data
public class SagaState {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sagaId;
	private Long productId;
	private Integer quantity;
	@Enumerated(EnumType.STRING)
	private SagaStatus status;

	public enum SagaStatus {
		PROCESSING_ORDER,
		PROCESSING_PAYMENT,
		PROCESSING_STOCK,
		COMPENSATING_PAYMENT,
		COMPENSATING_ORDER,
		COMPLETED,
		FAILED
	}
}
