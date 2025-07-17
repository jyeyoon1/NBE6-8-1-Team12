package com.caffe.domain.shipping.entity;

// 배송 상태를 나타내는 Enum
public enum ShippingStatus {
    TEMPORARY,        // 결제 전 임시 저장
    BEFORE_DELIVERY,  // 배송 전
    DELIVERING,       // 배송 중
    DELIVERED         // 배송 완료
}
