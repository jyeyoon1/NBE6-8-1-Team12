package com.caffe.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private String productName;
    private double price;
    private int total_quantity;
    private String description;
    private String image_url;
}