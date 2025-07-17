package com.caffe.domain.purchase.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseReqDto(
        @NotNull
        @Positive
        int productId,

        @Positive
        int price,

        @Positive
        int quantity,

        @Positive
        int totalPrice
) {
}
