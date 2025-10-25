package com.example.payment.dto;



public record PaymentReply(
    Long sagaId,
    String status
) {}
