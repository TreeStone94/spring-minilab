package com.example.payment.kafka;

import com.example.payment.dto.PaymentRequest;
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

    @KafkaListener(topics = "payment-requests")
    public void handlePaymentRequest(PaymentRequest event) {

        if("PROCESS_PAYMENT".equals(event.command())) {
            paymentService.processPayment(event);
        } else {
            paymentService.refundPayment(event);
        }

    }
}
