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
	private Long productId;
	private Integer quantity;
	private String status;
}
