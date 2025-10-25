package com.example.payment.kafka;

import com.example.payment.dto.PaymentReply;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentProcessedEvent(PaymentReply paymentReply) {
        log.info("Sending payment processed event: {}", paymentReply);
        kafkaTemplate.send("payment-replies", paymentReply);
    }
}
