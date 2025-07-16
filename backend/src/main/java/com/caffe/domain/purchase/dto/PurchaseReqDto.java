package com.caffe.domain.purchase.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseReqDto(
        @NotNull
        @Positive
        int productId,

        @Positive
        double price,

        @Positive
        int quantity,

        @Positive
        double totalPrice
) {
}
