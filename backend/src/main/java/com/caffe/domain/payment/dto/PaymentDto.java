package com.caffe.domain.payment.dto;

import com.caffe.domain.payment.entity.Payment;

import java.time.LocalDateTime;

public record PaymentDto(
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
