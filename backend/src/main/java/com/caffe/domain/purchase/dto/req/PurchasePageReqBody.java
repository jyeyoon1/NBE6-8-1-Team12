package com.caffe.domain.purchase.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PurchasePageReqBody(
        @Valid
        List<PurchaseItemReqDto> purchaseItems,

        @Valid
        PurchaserReqDto purchaser,

        @Valid
        ReceiverReqDto receiver,

        @NotNull
        int paymentOptionId
) {
}
