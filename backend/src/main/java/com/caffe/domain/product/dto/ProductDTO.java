package com.caffe.domain.product.dto;

import com.caffe.domain.product.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductDTO(
        @NotBlank(message = "상품명을 입력해주세요.")
        String productName,
        
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        int price,
        
        @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
        int totalQuantity,
        
        @Size(max = 1000, message = "설명은 최대 1000자까지 입력 가능합니다.")
        String description,
        
        String imageUrl
) {
    // 기본값으로 빈 ProductDTO 생성 (폼용)
    public static ProductDTO empty() {
        return new ProductDTO("", 0, 0, "", "");
    }
    
    // Entity에서 DTO로 변환
    public static ProductDTO from(Product product) {
        return new ProductDTO(
                product.getProductName(),
                product.getPrice(),
                product.getTotalQuantity(),
                product.getDescription(),
                product.getImageUrl()
        );
    }
    
    // DTO → Entity 변환 메서드
    public Product toEntity() {
        return new Product(
                this.productName,
                this.price,
                this.totalQuantity,
                this.description,
                this.imageUrl
        );
    }
}
