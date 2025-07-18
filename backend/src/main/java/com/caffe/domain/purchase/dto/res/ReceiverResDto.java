package com.caffe.domain.purchase.dto.res;

import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.constant.ShippingStatus;

public record ReceiverResDto(
        String name,
        String phoneNumber,
        String address,
        ShippingStatus status
) {
    public ReceiverResDto(Shipping shipping) {
        this (
                shipping.getContactName(),
                shipping.getContactNumber(),
                shipping.getAddress(),
                shipping.getStatus()
        );
    }
}
