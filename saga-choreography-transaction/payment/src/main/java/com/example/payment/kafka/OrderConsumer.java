package com.example.payment.kafka;

import com.example.payment.dto.OrderEvent;
import com.example.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConsumer {
    private final PaymentService paymentService;

    @KafkaListener(topics = "${app.kafka.topic.order-created}")
    public void consume(OrderEvent orderEvent) {
        System.out.println("Received order created event for order ID: " + orderEvent.orderId());
        paymentService.processPayment(orderEvent);
    }
}
