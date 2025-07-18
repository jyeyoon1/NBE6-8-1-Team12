package com.caffe.domain.payment.dto;

import com.caffe.domain.payment.entity.Payment;

import java.time.LocalDateTime;

public record PaymentRequestResponseDto(
        int id,
        String paymentOptionType,
        String paymentOptionName,
        int amount,
        LocalDateTime date
) {
    public PaymentRequestResponseDto(Payment payment) {
        this(
                payment.getId(),
                payment.getPaymentOption().getParent().getName(),
                payment.getPaymentOption().getName(),
                payment.getAmount(),
                payment.getModifyDate()
        );
    }
}
