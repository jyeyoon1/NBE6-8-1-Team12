package com.caffe.domain.purchase.service;

import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public Purchase getPurchaseById(int purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException(purchaseId + "번 주문을 찾을 수 없습니다."));
    }
}
