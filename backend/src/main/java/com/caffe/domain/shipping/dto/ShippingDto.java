package com.caffe.domain.shipping.dto;

import com.caffe.domain.shipping.constant.ShippingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingDto {

    private String email;         // 배송지와 연결된 이메일
    private String address;       // 배송 받을 주소
    private int postcode;         // 배송지 우편번호
    private String contactName;   // 수령인 이름
    private String contactNumber; // 연락처
    private ShippingStatus status;  // 배송 상태 (BEFORE_DELIVERY, DELIVERING, DELIVERED)
}