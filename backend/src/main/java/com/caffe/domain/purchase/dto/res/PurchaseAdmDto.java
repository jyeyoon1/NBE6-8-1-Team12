package com.caffe.domain.purchase.dto.res;

import com.caffe.domain.purchase.constant.PurchaseStatus;
import com.caffe.domain.purchase.entity.Purchase;

import java.time.LocalDateTime;

public record PurchaseAdmDto(
        int purchaseId,
        String userEmail,
        int totalPrice,
        int totalQuantity,
        PurchaseStatus purchaseStatus,
        LocalDateTime purchaseDate,
        String summaryName
) {
    public PurchaseAdmDto(Purchase purchase) {
        this(
                purchase.getId(),
                purchase.getUserEmail(),
                purchase.getTotalPrice(),
                purchase.getTotalQuantity(),
                purchase.getStatus(),
                purchase.getCreateDate(),
                purchase.summaryName()
        );
    }
}
