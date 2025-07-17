package com.caffe.domain.purchase.dto.res;

import com.caffe.domain.product.entity.Product;

public record PurchaseInfoDto(
        int productId,
        String productName,
        int price,
        String imageUrl,
        int quantity,
        int totalPrice
) {
    public PurchaseInfoDto(Product product, int quantity, int totalPrice) {
        this (
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getImageUrl(),
                quantity,
                totalPrice
        );
    }
}
