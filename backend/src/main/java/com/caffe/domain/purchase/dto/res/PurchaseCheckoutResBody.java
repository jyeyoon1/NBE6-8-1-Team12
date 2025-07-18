package com.caffe.domain.purchase.dto.res;

import com.caffe.domain.purchase.entity.Purchase;

public record PurchaseCheckoutResBody(
        int purchaseId,
        int paymentOptionId,
        int amount
) {
    public PurchaseCheckoutResBody(Purchase purchase, int paymentOptionId) {
        this (
                purchase.getId(),
                paymentOptionId,
                purchase.getTotalPrice()
        );
    }
}
