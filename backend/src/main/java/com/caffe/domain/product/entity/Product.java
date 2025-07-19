package com.caffe.domain.product.entity;

import com.caffe.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private ProductStatus status; // 상품 상태 추가

    public Product(String productName, int price, int totalQuantity, String description, String imageUrl, ProductStatus status) {
        this.productName = productName;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status != null ? status : ProductStatus.ON_SALE; // 상태가 null인 경우 기본값 설정
    }

    // 상품 정보 업데이트
    public void updateProductInfo(String productName, int price, int totalQuantity, String description, String imageUrl, ProductStatus status) {
        this.productName = productName;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status != null ? status : ProductStatus.ON_SALE; // 상태가 null인 경우 기본값 설정
    }

    public boolean hasStock(int quantity) {
        return this.totalQuantity >= quantity;
    }
}
