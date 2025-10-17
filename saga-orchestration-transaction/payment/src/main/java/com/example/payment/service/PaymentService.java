package com.example.payment.service;

import com.example.payment.entity.Payment;
import com.example.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void processPayment(Long orderId, double amount) {
        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .status(Payment.PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        // Simulate payment processing
        if (amount > 1000) { // Simulate failure condition
            payment.setStatus(Payment.PaymentStatus.FAILED);
        } else {
            payment.setStatus(Payment.PaymentStatus.SUCCESSFUL);
        }
        paymentRepository.save(payment);
    }
}
