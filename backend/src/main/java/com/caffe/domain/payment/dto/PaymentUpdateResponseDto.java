package com.caffe.domain.payment.dto;

import com.caffe.domain.payment.entity.Payment;

import java.time.LocalDateTime;

public record PaymentUpdateResponseDto(int id,
                                       String paymentOptionType,
                                       String paymentOptionName,
                                       String paymentInfo,
                                       int amount,
                                       LocalDateTime date
) {
    public PaymentUpdateResponseDto(Payment payment) {
        this(
                payment.getId(),
                payment.getPaymentOption().getParent().getName(),
                payment.getPaymentOption().getName(),
                payment.getPaymentInfo(),
                payment.getAmount(),
                payment.getModifyDate()
        );
    }
}
