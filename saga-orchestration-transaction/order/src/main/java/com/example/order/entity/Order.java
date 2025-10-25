package com.example.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.aspectj.weaver.ast.Or;

@Entity
@Table(name = "orders")
@Data
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	private Long productId;
	private Integer quantity;
	private OrderStatus status;

	public enum OrderStatus {
		CREATED,
		CANCELLED
	}
}
