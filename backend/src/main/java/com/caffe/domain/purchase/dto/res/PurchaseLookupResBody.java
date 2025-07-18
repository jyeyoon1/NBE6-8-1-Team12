package com.caffe.domain.purchase.dto.res;

import com.caffe.domain.purchase.entity.Purchase;

public record PurchaseLookupResBody(
        int purchaseId,
        String userEmail
) {
    public PurchaseLookupResBody(Purchase purchase) {
        this (
                purchase.getId(),
                purchase.getUserEmail()
        );
    }
}
