package com.caffe.domain.payment.dto;


import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.constant.PaymentOptionType;

public record PaymentDetailOptionDto(
        int id,
        Integer parentId,
        String name,
        PaymentOptionType type,
        String code,
        int sortSeq
) {
    public PaymentDetailOptionDto(PaymentOption paymentOption) {
        this(
                paymentOption.getId(),
                (paymentOption.getParent() != null) ? paymentOption.getParent().getId() : null,
                paymentOption.getName(),
                paymentOption.getType(),
                paymentOption.getCode(),
                paymentOption.getSortSeq()
        );
    }
}
