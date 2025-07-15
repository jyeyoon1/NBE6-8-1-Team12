package com.caffe.domain.shipping.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingDto {
    private int purchaseId;
    private String address;
    private String postcode;
}
