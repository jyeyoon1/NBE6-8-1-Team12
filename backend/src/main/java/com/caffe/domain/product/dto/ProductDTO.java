package com.caffe.domain.product.dto;

import com.caffe.domain.product.entity.Product;
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

    // DTO → Entity 변환 메서드 (예시)
    public Product toEntity() {
        Product p = new Product();
        p.setProductName(this.productName);
        p.setDescription(this.description);
        p.setPrice(this.price);
        p.setTotal_quantity(this.total_quantity);
        p.setImage_url(this.image_url);
        return p;
    }
}