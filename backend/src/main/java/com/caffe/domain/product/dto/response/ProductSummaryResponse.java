package com.caffe.domain.product.dto.response;

import com.caffe.domain.product.entity.Product;

/**
 * 상품 목록 조회 응답 DTO (간단한 정보만)
 */
public record ProductSummaryResponse(
        int id,
        String productName,
        int price,
        String imageUrl,
        int totalQuantity,
        boolean isInStock  // 재고 여부 (비즈니스 로직 결과)
) {
    public ProductSummaryResponse(Product product) {
        this(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getTotalQuantity(),
                product.getTotalQuantity() > 0  // 재고 여부 계산
        );
    }
}
