package com.example.payment.service;

import com.example.payment.dto.PaymentRequest;
import com.example.payment.dto.PaymentReply;
import com.example.payment.entity.Payment;
import com.example.payment.kafka.PaymentProducer;
import com.example.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    @Transactional
    public void processPayment(PaymentRequest event) {
        Payment payment = Payment.builder()
                .sagaId(event.sagaId())
                .status(Payment.PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        if (payment.getSagaId() % 2 == 0) { // 실패 처리
            payment.setStatus(Payment.PaymentStatus.FAILED);
        } else {
            payment.setStatus(Payment.PaymentStatus.SUCCESSFUL);
        }
        paymentRepository.save(payment);

        String paymentReplyStatus = "PAYMENT_PROCESSED";
        if (payment.getStatus() == Payment.PaymentStatus.FAILED) {
            paymentReplyStatus = "PAYMENT_FAILED";
        }
        PaymentReply paymentReply = new PaymentReply(payment.getSagaId(), paymentReplyStatus);
        paymentProducer.sendPaymentProcessedEvent(paymentReply);
    }

    public void refundPayment(PaymentRequest event) {
        Payment payment = paymentRepository.findBySagaId(event.sagaId());
        if (payment != null) {
            payment.setStatus(Payment.PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }
    }
}
