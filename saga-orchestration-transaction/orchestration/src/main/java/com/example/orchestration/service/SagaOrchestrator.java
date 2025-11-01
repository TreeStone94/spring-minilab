package com.example.orchestration.service;

import com.example.orchestration.dto.*;
import com.example.orchestration.entity.SagaState;
import com.example.orchestration.kafka.OrchestratorProducer;
import com.example.orchestration.repository.SagaStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SagaOrchestrator {
	private final OrchestratorProducer orchestratorProducer;
	private final SagaStateRepository sagaStateRepository;
	@Transactional
	public void startSaga(OrderRequest orderRequest) {
		SagaState sagaState = new SagaState();
		sagaState.setProductId(orderRequest.productId());
		sagaState.setQuantity(orderRequest.quantity());
		sagaState.setStatus(SagaState.SagaStatus.PROCESSING_ORDER);
		SagaState savedSagaState = sagaStateRepository.save(sagaState);

		OrderServiceRequest orderCommand = new OrderServiceRequest("CREATE_ORDER", sagaState.getSagaId(), savedSagaState.getProductId(), savedSagaState.getQuantity());
		orchestratorProducer.sendToOrderService(orderCommand);
		log.error("Saga started. SagaId: {}", savedSagaState.getSagaId());
	}

	@KafkaListener(topics = "order-replies")
	@Transactional
	public void handleOrderReply(OrderReply reply) {
		SagaState sagaState = sagaStateRepository.findById(reply.sagaId()).orElse(null);
		if(sagaState == null) {
			return;
		}

		if("ORDER_CREATED".equals(reply.status())) {
			// 주문 성공 -> 상태 변경 및 결제 요청
			sagaState.setStatus(SagaState.SagaStatus.PROCESSING_PAYMENT);
			sagaStateRepository.save(sagaState);

			log.error("Order created, processing payment for sagaId: {}", reply.sagaId());
			PaymentServiceRequest paymentCommand = new PaymentServiceRequest("PROCESS_PAYMENT", reply.sagaId(), sagaState.getProductId());
			orchestratorProducer.sendToPaymentService(paymentCommand);
		} else {
			// 주문 실패 -> Saga 실패 상태로 변경
			sagaState.setStatus(SagaState.SagaStatus.ORDER_FAILED);
			sagaStateRepository.save(sagaState);
			log.error("Order failed, ending saga for sagaId: {}", reply.sagaId());
		}
	}

	@KafkaListener(topics = "payment-replies")
   @Transactional
   public void handlePaymentReply(PaymentReply reply) {
       SagaState sagaState = sagaStateRepository.findById(reply.sagaId()).orElse(null);
       if (sagaState == null) return;

       if ("PAYMENT_PROCESSED".equals(reply.status())) {
           // 결제 성공 -> 상태 변경 및 재고 감소 요청
           sagaState.setStatus(SagaState.SagaStatus.PROCESSING_STOCK);
           sagaStateRepository.save(sagaState);

           log.error("Payment processed, decreasing stock for sagaId: {}", reply.sagaId());
           StockServiceRequest stockCommand = new StockServiceRequest("DECREASE_STOCK", reply.sagaId(), sagaState.getProductId(), sagaState.getQuantity());
	       orchestratorProducer.sendToStockService(stockCommand);
       } else {
           // 결제 실패 -> 상태 변경 및 주문 취소(보상) 요청
           sagaState.setStatus(SagaState.SagaStatus.PAYMENT_FAILED);
           sagaStateRepository.save(sagaState);

           log.error("Payment failed, compensating order for sagaId: {}", reply.sagaId());
           OrderServiceRequest orderCommand = new OrderServiceRequest("CANCEL_ORDER", reply.sagaId(), null, 0);
	       orchestratorProducer.sendToOrderService(orderCommand);
       }
   }

	@KafkaListener(topics = "stock-replies")
	@Transactional
	public void handleStockReply(StockReply reply) {
		SagaState sagaState = sagaStateRepository.findById(reply.sagaId()).orElse(null);
		if (sagaState == null) return;

		if ("STOCK_DECREASED".equals(reply.status())) {
			// 재고 감소 성공 -> Saga 완료
			sagaState.setStatus(SagaState.SagaStatus.COMPENSATING_STOCK);
			sagaStateRepository.save(sagaState);
			log.error("Saga completed successfully for sagaId: {}", reply.sagaId());
		} else {
			// 재고 부족 -> 상태 변경 및 결제 환불(보상) 요청
			sagaState.setStatus(SagaState.SagaStatus.STOCK_FAILED);
			sagaStateRepository.save(sagaState);

			log.error("Stock failed, compensating payment and order for sagaId: {}", reply.sagaId());
			PaymentServiceRequest paymentCommand = new PaymentServiceRequest("REFUND_PAYMENT", reply.sagaId(), null);
			orchestratorProducer.sendToPaymentService(paymentCommand);
			// 결제 환불 후 주문 취소는 Payment 서비스의 보상 트랜잭션이 완료된 후에 진행할 수도 있으나,
			// 이 예제에서는 단순화를 위해 바로 요청합니다.
			OrderServiceRequest orderCommand = new OrderServiceRequest("CANCEL_ORDER", reply.sagaId(), null, 0);
			orchestratorProducer.sendToOrderService(orderCommand);
		}
	}

}
