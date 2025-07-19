package com.caffe.domain.purchase.dto.res;

import java.util.List;

public record PurchaseDetailDto(
        PurchaseDto purchase,
        List<PurchaseItemDetailDto> purchaseItems,
        ReceiverResDto receiver
) {
}
