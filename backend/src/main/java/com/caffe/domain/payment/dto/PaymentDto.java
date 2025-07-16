package com.caffe.domain.payment.dto;

import com.caffe.domain.payment.entity.Payment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record PaymentResponseDto(
        int id,
        String method,
        char status,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        int purchaseId
) {
    public PaymentDto(Payment payment){
        this(
                payment.getId(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getCreateDate(),
                payment.getModifyDate(),
                payment.getPurchase().getId()
        );
    }
}

public record PaymentRequestDto(
        @NotNull int purchaseId,
        @NotNull int paymentOptionId,
        @Positive double amount
) {}