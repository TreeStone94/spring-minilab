package com.example.stock.service;

import com.example.stock.dto.StockReply;
import com.example.stock.dto.StockRequest;
import com.example.stock.entity.Stock;
import com.example.stock.kafka.StockProducer;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
	private final StockRepository stockRepository;
	private final StockProducer stockProducer;

	@Transactional
	public void decreaseStock(StockRequest request) {
		StockReply reply;
		Stock stock = stockRepository.findByProductId(request.productId()).orElse(null);

		if (stock != null && stock.getQuantity() >= request.quantity()) {
			stock.setQuantity(stock.getQuantity() - request.quantity());
			stockRepository.save(stock);
			reply = new StockReply(request.sagaId(), "STOCK_DECREASED");
			log.info("Stock decreased for productId: {}, new quantity: {}", request.productId(), stock.getQuantity());
		} else {
			reply = new StockReply(request.sagaId(), "STOCK_DECREASE_FAILED");
			log.warn("Stock decrease failed for productId: {}. Insufficient stock or product not found.", request.productId());
		}
		stockProducer.sendStockReply(reply);
	}

	@Transactional
	public void increaseStock(StockRequest request) {
		StockReply reply;
		Stock stock = stockRepository.findByProductId(request.productId()).orElse(null);

		if (stock != null) {
			stock.setQuantity(stock.getQuantity() + request.quantity());
			stockRepository.save(stock);
			reply = new StockReply(request.sagaId(), "STOCK_INCREASED");
			log.info("Stock increased for productId: {}, new quantity: {}", request.productId(), stock.getQuantity());
		} else {
			// 재고 증가 실패 (상품을 찾을 수 없는 경우)
			reply = new StockReply(request.sagaId(), "STOCK_INCREASE_FAILED");
			log.error("Stock increase failed for productId: {}. Product not found.", request.productId());
		}
		stockProducer.sendStockReply(reply);
	}
}
