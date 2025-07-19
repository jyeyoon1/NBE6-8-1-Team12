package com.caffe.domain.purchase.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemReqBody(
        @NotNull
        @Positive
        int productId,

        @NotNull
        @Positive
        int quantity
) {
}
