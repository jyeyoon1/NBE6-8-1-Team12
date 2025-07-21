package com.caffe.domain.payment.dto;

import com.caffe.domain.payment.constant.PaymentStatus;
import com.caffe.domain.payment.entity.Payment;

import java.time.LocalDateTime;

public record PaymentItemResponseDto(int id,
                                     String paymentOptionType,
                                     String paymentOptionName,
                                     String paymentInfo,
                                     int amount,
                                     PaymentStatus paymentStatus,
                                     int purchaseId,
                                     String purchaseEmail,
                                     LocalDateTime date
) {
    public PaymentItemResponseDto(Payment payment) {
        this(
                payment.getId(),
                payment.getPaymentOption().getParent().getName(),
                payment.getPaymentOption().getName(),
                payment.getPaymentInfo(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPurchase().getId(),
                payment.getPurchase().getUserEmail(),
                payment.getModifyDate()
        );
    }
}
