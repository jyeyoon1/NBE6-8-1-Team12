package com.caffe.domain.shipping.entity;

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
        this.postcode = shipping.getPostcode(); // Shipping 엔티티에 postcode 필드 있어야 함
        this.contactName = shipping.getContactName();
        this.contactNumber = shipping.getContactNumber();
        this.carrier = shipping.getCarrier();
        this.status = shipping.getStatus().name();
    }
}
