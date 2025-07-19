package com.caffe.domain.purchase.dto.res;

import java.util.List;

public record PurchaseDetailDto2(
        PurchaseDto purchase,
        List<PurchaseItemDetailDto> purchaseItems,
        ReceiverResDto receiver
) {
}
