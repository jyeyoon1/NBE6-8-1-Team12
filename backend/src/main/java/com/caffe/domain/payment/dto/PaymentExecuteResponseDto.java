package com.caffe.domain.payment.dto;

import com.caffe.domain.payment.constant.PaymentStatus;
import com.caffe.domain.payment.entity.Payment;

import java.time.LocalDateTime;

public record PaymentExecuteResponseDto(int id,
                                        PaymentStatus paymentStatus,
                                        int purchaseId,
                                        String userEmail,
                                        LocalDateTime date
) {
    public PaymentExecuteResponseDto(Payment payment) {
        this(
                payment.getId(),
                payment.getStatus(),
                payment.getPurchase().getId(),
                payment.getPurchase().getUserEmail(),
                payment.getModifyDate()
        );
    }
}
