package com.caffe.domain.product.dto;

import com.caffe.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private String productName;
    private double price;
    private int totalQuantity;
    private String description;
    private String imageUrl;

    // DTO → Entity 변환 메서드 (예시)
    public Product toEntity() {
        Product p = new Product();
        p.setProductName(this.productName);
        p.setDescription(this.description);
        p.setPrice(this.price);
        p.setTotalQuantity(this.totalQuantity);
        p.setImageUrl(this.imageUrl);
        return p;
    }
}