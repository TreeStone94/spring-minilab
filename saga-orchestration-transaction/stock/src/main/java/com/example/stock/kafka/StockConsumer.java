package com.example.stock.kafka;

import com.example.stock.dto.StockRequest;
import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockConsumer {
	private final StockService stockService;

	@KafkaListener(topics = "stock-requests")
	public void handleStockRequest(StockRequest request) {
		if ("DECREASE_STOCK".equals(request.command())) {
			stockService.decreaseStock(request);
		} else if ("INCREASE_STOCK".equals(request.command())) {
			stockService.increaseStock(request);
		}
	}
}
