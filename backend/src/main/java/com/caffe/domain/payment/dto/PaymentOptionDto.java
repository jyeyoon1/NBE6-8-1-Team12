package com.caffe.domain.payment.dto;

import com.caffe.domain.payment.entity.PaymentOption;
import jakarta.validation.constraints.NotNull;

public record PaymentOptionDto(
        @NotNull
        int id,
        @NotNull
        String name
) {
    public PaymentOptionDto(PaymentOption paymentOption) {
        this(paymentOption.getId(), paymentOption.getName());
    }
}
