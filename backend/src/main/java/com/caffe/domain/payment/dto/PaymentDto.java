package com.caffe.domain.payment.dto;

import com.caffe.domain.payment.entity.Payment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class PaymentDto {

    public record PaymentResponseDto(
            int id,
            String paymentOptionType,
            String paymentOptionName,
            String paymentInfo,
            double amount,
            char status,
            LocalDateTime createDate,
            LocalDateTime modifyDate,
            int purchaseId
    ) {
        public PaymentResponseDto(Payment payment) {
            this(
                    payment.getId(),
                    payment.getPaymentOption().getType().toString(),
                    payment.getPaymentOption().getName(),
                    payment.getPaymentInfo(),
                    payment.getAmount(),
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
            @NotNull String paymentInfo,
            @Positive double amount
    ) {
    }

    public record PaymentUpdateDto(
            @NotNull int paymentId,
            @NotNull int paymentOptionId,
            @NotNull String paymentInfo,
            @Positive double amount
    ) {
    }
}