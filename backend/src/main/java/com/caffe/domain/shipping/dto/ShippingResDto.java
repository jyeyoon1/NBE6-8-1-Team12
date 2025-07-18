package com.caffe.domain.shipping.dto;

import com.caffe.domain.shipping.entity.Shipping;
import lombok.Getter;

@Getter
public class ShippingResDto {

    private int id;
    private String address;
    private int postcode;
    private String contactName;
    private String contactNumber;
    private String carrier;
    private String status;

    public ShippingResDto(Shipping shipping) {
        this.id = shipping.getId();
        this.address = shipping.getAddress();
        this.postcode = shipping.getPostcode();
        this.contactName = shipping.getContactName();
        this.contactNumber = shipping.getContactNumber();
        this.carrier = shipping.getCarrier();
        this.status = shipping.getStatus().name();
    }
}