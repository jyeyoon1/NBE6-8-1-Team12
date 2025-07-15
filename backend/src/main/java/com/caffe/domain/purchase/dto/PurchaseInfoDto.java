package com.caffe.domain.purchase.dto;

import com.caffe.domain.product.entity.Product;

public record PurchaseInfoDto(
        int productId,
        String productName,
        double price,
        String image_url,
        int quantity,
        double totalPrice
) {
    public PurchaseInfoDto(Product product, int quantity, double totalPrice) {
        this (
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getImage_url(),
                quantity,
                totalPrice
        );
    }
}
