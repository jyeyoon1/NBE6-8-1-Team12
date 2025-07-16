package com.caffe.domain.purchase.dto.res;

public record PurchaseDetailDto(
        PurchaseDto purchase,
        PurchaseItemDetailDto purchaseItem, // 목록으로 전환 예정
        ReceiverResDto receiver
) {
}
