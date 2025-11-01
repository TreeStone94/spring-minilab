package com.example.stock.dto;

public record StockRequest(String type, Long sagaId, Long productId, int quantity) {
}
