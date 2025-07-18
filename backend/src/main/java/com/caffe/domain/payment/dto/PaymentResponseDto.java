package com.caffe.domain.payment.dto;

import com.caffe.domain.payment.entity.Payment;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.global.constant.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponseDto(int id,
                                 String paymentOptionType,
                                 String paymentOptionName,
                                 String paymentInfo,
                                 int amount,
                                 PaymentStatus paymentStatus,
                                 int purchaseId,
                                 LocalDateTime date
) {
    public PaymentResponseDto(Payment payment) {
        this(
                payment.getId(),
                payment.getPaymentOption().getParent().getName(),
                payment.getPaymentOption().getName(),
                payment.getPaymentInfo(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPurchase().getId(),
                payment.getModifyDate()
        );
    }
}
