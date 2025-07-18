package com.caffe.domain.product.dto.response;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.entity.ProductStatus;

import java.time.LocalDateTime;

/**
 * 상품 상세 조회 응답 DTO (모든 정보)
 */
public record ProductDetailResponse(
        int id,
        String productName,
        int price,
        int totalQuantity,
        String description,
        String imageUrl,
        boolean isInStock,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        String priceFormatted,  // 가격 포맷팅 (예: "1,000원")
        String stockStatus,      // 재고 상태 (예: "충분", "부족", "품절")
        ProductStatus status
) {
    public ProductDetailResponse(Product product) {
        this(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getTotalQuantity(),
                product.getDescription(),
                product.getImageUrl(),
                product.getTotalQuantity() > 0,
                product.getCreateDate(),
                product.getModifyDate(),
                String.format("%,d원", product.getPrice()),
                getStockStatus(product.getTotalQuantity()),
                product.getStatus()
        );
    }
    
    private static String getStockStatus(int quantity) {
        if (quantity == 0) return "품절";
        if (quantity < 10) return "부족";
        return "충분";
    }
}
