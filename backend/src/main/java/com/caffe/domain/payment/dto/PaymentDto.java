package com.caffe.domain.payment.dto;

import com.caffe.domain.payment.entity.Payment;
import com.caffe.domain.payment.entity.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class PaymentDto {

    public record PaymentResponseDto(
            int id,
            String paymentOptionType,
            String paymentOptionName,
            String paymentInfo,
            int amount,
            PaymentStatus status,
            LocalDateTime createDate,
            LocalDateTime modifyDate,
            int purchaseId
    ) {
        public PaymentResponseDto(Payment payment) {
            this(
                    payment.getId(),
                    payment.getPaymentOption().getParent().getName(),
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
            @Positive int amount
    ) {
    }

    public record PaymentExecuteDto(
            @NotNull String paymentInfo
    ) {
    }

    public record PaymentUpdateDto(
            @NotNull int paymentOptionId,
            @NotNull String paymentInfo,
            @Positive int amount
    ) {
    }
}