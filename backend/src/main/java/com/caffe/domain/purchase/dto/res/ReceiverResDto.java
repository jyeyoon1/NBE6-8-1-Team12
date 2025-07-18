package com.caffe.domain.purchase.dto.res;

import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.entity.ShippingStatus;

public record ReceiverResDto(
        String name,
        String phoneNumber,
        int postcode,
        String address,
        ShippingStatus status
) {
    public ReceiverResDto(Shipping shipping) {
        this (
                shipping.getContactName(),
                shipping.getContactNumber(),
                shipping.getPostcode(),
                shipping.getAddress(),
                shipping.getStatus()
        );
    }
}
