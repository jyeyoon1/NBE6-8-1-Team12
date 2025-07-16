package com.caffe.domain.purchase.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PurchasePageReqBody(
        @Valid
        PurchaseReqDto purchase,

        @Valid
        PurchaserReqDto purchaser,

        @Valid
        ReceiverReqDto receiver,

        @NotNull
        int paymentOptionId
) {
}
