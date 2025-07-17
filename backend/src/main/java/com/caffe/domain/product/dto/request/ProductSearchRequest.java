//package com.caffe.domain.product.dto.request;
//
///**
// * 상품 검색 요청 DTO
// */
//public record ProductSearchRequest(
//        String keyword,
//        Integer minPrice,
//        Integer maxPrice,
//        String sortBy,  // "price", "name", "createDate"
//        String sortDirection  // "asc", "desc"
//) {
//    public static ProductSearchRequest empty() {
//        return new ProductSearchRequest(null, null, null, "createDate", "desc");
//    }
//}


// 검색 기능을 위해서 임시로 만들어 두었음