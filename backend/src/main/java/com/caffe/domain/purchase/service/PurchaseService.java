package com.caffe.domain.purchase.service;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import com.caffe.domain.purchase.dto.PurchaseInfoDto;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ProductService productService;

    public Purchase getPurchaseById(int purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException(purchaseId + "번 주문을 찾을 수 없습니다."));
    }

    public PurchaseInfoDto getOrderPageInfo(int productId, int quantity) {
        Product product = productService.getProductById(productId);
        double totalPrice = calculateTotalPrice(product.getPrice(), quantity);

        return new PurchaseInfoDto(product, quantity, totalPrice);
    }

    public double calculateTotalPrice(double price, int quantity) {
        return price * quantity;
    }
}
