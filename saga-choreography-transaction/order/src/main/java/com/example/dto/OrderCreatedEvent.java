package com.example.dto;

public record OrderCreatedEvent(Long orderId, Long productId, int quantity) {}

