package com.caffe.domain.payment.controller;

import com.caffe.domain.payment.dto.PaymentOptionDto;
import com.caffe.domain.payment.service.PaymentOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payment-options")
@RequiredArgsConstructor
public class PaymentOptionController {
    private final PaymentOptionService paymentOptionService;

    @GetMapping
    public List<PaymentOptionDto> getDetailPaymentOptions(@RequestParam int parentId) {
        return paymentOptionService.getDetailPaymentOptions(parentId);
    }
}
