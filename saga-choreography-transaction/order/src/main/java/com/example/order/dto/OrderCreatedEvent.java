package com.example.order.dto;

public record OrderCreatedEvent(Long orderId, Long productId, int quantity) {}

