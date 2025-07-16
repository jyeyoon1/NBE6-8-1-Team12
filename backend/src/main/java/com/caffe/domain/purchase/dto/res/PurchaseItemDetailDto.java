package com.caffe.domain.purchase.dto.res;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.purchase.entity.PurchaseItem;

public record PurchaseItemDetailDto(
        int purchaseItemId,
        int quantity,
        double price,
        String productName,
        String imageUrl
) {
    public PurchaseItemDetailDto(PurchaseItem purchaseItem, Product product) {
        this(
                purchaseItem.getId(),
                purchaseItem.getQuantity(),
                purchaseItem.getPrice(),
                product.getProductName(),
                product.getImageUrl()
        );
    }
}
