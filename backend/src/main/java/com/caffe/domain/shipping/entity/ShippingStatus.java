package com.caffe.domain.shipping.entity;

// 배송 상태를 나타내는 Enum
public enum ShippingStatus {
    BEFORE_DELIVERY,  // 배송 전
    DELIVERING,       // 배송 중
    DELIVERED         // 배송 완료
}
