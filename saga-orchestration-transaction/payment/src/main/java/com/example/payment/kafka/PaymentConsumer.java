package com.example.payment.kafka;

import com.example.payment.dto.OrderCreatedEvent;
import com.example.payment.entity.Payment;
import com.example.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {

    private final PaymentService paymentService;
    private final PaymentProducer paymentProducer;

    @KafkaListener(topics = "payment-requests")
    public void listenOrderCreated(OrderCreatedEvent event) {
        log.info("Received order created event: {}", event);
        try {
            paymentService.processPayment(event.orderId(), event.amount());

            // Simulate checking payment status after processing
            Payment.PaymentStatus status = event.amount() > 1000 ? Payment.PaymentStatus.FAILED : Payment.PaymentStatus.SUCCESSFUL;

            paymentProducer.sendPaymentProcessedEvent(event.orderId(), status.name());

        } catch (Exception e) {
            log.error("Failed to process payment for order {}", event.orderId(), e);
            // Optionally, send a payment failed event
            paymentProducer.sendPaymentProcessedEvent(event.orderId(), Payment.PaymentStatus.FAILED.name());
        }
    }
}
