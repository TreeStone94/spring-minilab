package com.example.payment.dto;



public record PaymentProcessedEvent (
    Long orderId,
    String status
) {}
