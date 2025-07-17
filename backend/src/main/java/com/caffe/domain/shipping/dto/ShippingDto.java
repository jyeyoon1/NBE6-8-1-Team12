package com.caffe.domain.shipping.dto;

import com.caffe.domain.shipping.entity.ShippingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingDto {

    private int purchaseId;    // 배송이 연결될 구매(Purchase)의 ID
    private String address;    // 배송 받을 주소
    private int postcode;   // 배송지 우편번호
    private ShippingStatus status;  // 배송 상태 (BEFORE_DELIVERY, DELIVERING, DELIVERED)

}