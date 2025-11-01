package com.example.stock.kafka;

import com.example.stock.dto.StockReply;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void sendStockReply(StockReply reply) {
		kafkaTemplate.send("stock-replies", reply);
	}
}
