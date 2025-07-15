package com.caffe.domain.payment.controller;

import com.caffe.domain.payment.dto.PaymentDto;
import com.caffe.domain.payment.entity.Payment;
import com.caffe.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public PaymentDto findById(@PathVariable int id) {
        Payment payment = paymentService.findById(id).get();
        return new PaymentDto(payment);
    }
}
