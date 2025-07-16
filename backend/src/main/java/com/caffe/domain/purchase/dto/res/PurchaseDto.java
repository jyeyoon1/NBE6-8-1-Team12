package com.caffe.domain.purchase.dto.res;

import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.entity.PurchaseStatus;

import java.time.LocalDateTime;

public record PurchaseDto(
        int purchaseId,
        String userEmail,
        double totalPrice,
        PurchaseStatus purchaseStatus,
        LocalDateTime purchaseDate
) {
    public PurchaseDto(Purchase purchase) {
        this(
                purchase.getId(),
                purchase.getUserEmail(),
                purchase.getTotalPrice(),
                purchase.getStatus(),
                purchase.getCreateDate()
        );
    }
}
