package com.caffe.domain.shipping.dto;

import com.caffe.domain.shipping.entity.Shipping;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ShippingResDto {

    private int id;
    private String address;
    private int postcode;
    private String contactName;
    private String contactNumber;
    private String carrier;
    private String status;
    private String email;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    public ShippingResDto(Shipping shipping) {
        this.id = shipping.getId();
        this.address = shipping.getAddress();
        this.postcode = shipping.getPostcode();
        this.contactName = shipping.getContactName();
        this.contactNumber = shipping.getContactNumber();
        this.carrier = shipping.getCarrier();
        this.status = shipping.getStatus().name();
        this.email = shipping.getEmail();
        this.createDate = shipping.getCreateDate();
    }
}