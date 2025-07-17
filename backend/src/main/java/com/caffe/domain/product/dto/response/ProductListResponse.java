package com.caffe.domain.product.dto.response;

import java.util.List;

/**
 * 상품 목록 전체 응답 DTO (페이징 정보 포함)
 */
public record ProductListResponse(
        List<ProductSummaryResponse> products,
        int totalCount,
        int currentPage,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
    // 페이징 없는 간단한 목록용
    public static ProductListResponse of(List<ProductSummaryResponse> products) {
        return new ProductListResponse(
                products,
                products.size(),
                1,
                1,
                false,
                false
        );
    }
    
    // 페이징 있는 목록용
    public static ProductListResponse of(List<ProductSummaryResponse> products, 
                                       int totalCount, 
                                       int currentPage, 
                                       int pageSize) {
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        return new ProductListResponse(
                products,
                totalCount,
                currentPage,
                totalPages,
                currentPage < totalPages,
                currentPage > 1
        );
    }
}
