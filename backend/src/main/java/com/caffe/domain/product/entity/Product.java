package com.caffe.domain.product.entity;

import com.caffe.global.exception.BusinessLogicException;
import com.caffe.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
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
    
    @Column(length = 1000)
    private String description;
    
    @Column(length = 500)
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

    public void decreaseStock(int quantity) {
        if (!hasStock(quantity)) {
            throw new BusinessLogicException("409-1", "%s의 재고가 부족합니다.".formatted(this.productName));
        }
        this.totalQuantity -= quantity;

        // 재고 0일 시 재고소진 상태로 변경
        if (this.totalQuantity == 0) {
            this.status = ProductStatus.OUT_OF_STOCK;
        }
    }

    public void restoreStock(int quantity) {
        this.totalQuantity += quantity;
    }

    // 상품 상태만 변경
    public void updateStatus(ProductStatus status) {
        this.status = status != null ? status : ProductStatus.ON_SALE;
    }

    // 재고만 변경
    public void updateStock(int totalQuantity) {
        if (totalQuantity < 0) {
            throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
        }
        this.totalQuantity = totalQuantity;
    }

}
