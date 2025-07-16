package com.caffe.domain.purchase.dto;

import com.caffe.domain.product.entity.Product;

public record PurchaseInfoDto(
        int productId,
        String productName,
        double price,
        String imageUrl,
        int quantity,
        double totalPrice
) {
    public PurchaseInfoDto(Product product, int quantity, double totalPrice) {
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
