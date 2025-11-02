package com.example.order.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	private Long sagaId; // sagaId 필드 추가
	private Long productId;
	private Integer quantity;
	@Enumerated(EnumType.STRING) // Enum 타입을 문자열로 저장
	private OrderStatus status;

	public enum OrderStatus {
		CREATED,
		CANCELLED
	}
}
