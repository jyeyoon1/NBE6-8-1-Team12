package com.caffe.domain.purchase.dto;

import com.caffe.domain.product.entity.Product;

public record PurchasePageResBody(
        int productId,
        String userEmail,
        String productName,
        double price,
        String image_url,
        int quantity,
        double totalPrice
) {
    public static PurchasePageResBody of(Product product, String userEmail, int quantity, double totalPrice) {
        return new PurchasePageResBody(
                product.getId(),
                userEmail,
                product.getProductName(),
                product.getPrice(),
                product.getImage_url(),
                quantity,
                totalPrice
        );
    }
}
