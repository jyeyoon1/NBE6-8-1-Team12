package com.caffe.domain.product.dto.request;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.entity.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 상품 수정 요청 DTO
 */
public record ProductUpdateRequest(
        @NotBlank(message = "상품명을 입력해주세요.")
        String productName,
        
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        int price,
        
        @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
        int totalQuantity,
        
        @Size(max = 1000, message = "설명은 최대 1000자까지 입력 가능합니다.")
        String description,
        
        String imageUrl,

        ProductStatus status
) {
    public ProductUpdateRequest(Product product) {
        this(
                product.getProductName(),
                product.getPrice(),
                product.getTotalQuantity(),
                product.getDescription(),
                product.getImageUrl(),
                product.getStatus()
        );
    }
}
