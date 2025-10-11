package com.example.order.kafka;

import com.example.dto.PaymentEvent;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "${app.kafka.topic.payment-events}", groupId = "order-group")
    public void consume(PaymentEvent event) {
        System.out.println("Received payment event: " + event.status() + " for order ID: " + event.overId());
        orderService.updateOrderStatus(event.overId(), event.status());
    }
}
