package com.caffe.domain.purchase.dto;

import com.caffe.domain.payment.dto.PaymentOptionDto;

import java.util.List;

public record PurchasePageResBody(
        PurchaseInfoDto purchaseInfo,
        List<PaymentOptionDto> paymentOptions
) {
}
