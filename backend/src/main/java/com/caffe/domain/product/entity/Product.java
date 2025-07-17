package com.caffe.domain.product.entity;

import com.caffe.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    private String productName;
    private int price;
    private int totalQuantity;
    private String description;
    private String imageUrl;

    public Product(String productName, int price, int totalQuantity, String description, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // 상품 정보 업데이트를 위한 비즈니스 메서드
    public void updateProductInfo(String productName, int price, int totalQuantity, String description, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
