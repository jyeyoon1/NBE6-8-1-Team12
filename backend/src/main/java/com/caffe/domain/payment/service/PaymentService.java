package com.caffe.domain.payment.service;

import com.caffe.domain.payment.entity.Payment;
import com.caffe.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Optional<Payment> findById(int id) {
        return paymentRepository.findById(id);
    }
}
