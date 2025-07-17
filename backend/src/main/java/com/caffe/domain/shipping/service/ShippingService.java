package com.caffe.domain.shipping.service;

import com.caffe.domain.purchase.dto.req.ReceiverReqDto;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.repository.PurchaseRepository;
import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.entity.ShippingStatus;
import com.caffe.domain.shipping.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShippingService {

    private final ShippingRepository shippingRepository;
    private final PurchaseRepository purchaseRepository;

    /*
     - 배송 정보 생성
     - 매개변수: Purchase, ReceiverDto
     - 배송 상태는 기본적으로 BEFORE_DELIVERY가 설정됨
    */
    public Shipping createShipping(ReceiverReqDto receiver, Purchase purchase) {
        Shipping shipping = new Shipping(
                receiver.address(),
                receiver.postcode(),
                receiver.phoneNumber(),
                receiver.name(),
                "CJ대한통운", // TODO: carrier 선택 기능 향후 추가
                ShippingStatus.TEMPORARY,
                purchase
        );

        return shippingRepository.save(shipping);
    }


    // 구매 ID로 Purchase 엔티티를 조회
    public Purchase getPurchaseById(int purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 구매를 찾을 수 없습니다."));
    }

    // 구매 ID로 배송 조회
    public Optional<Shipping> getShippingByPurchaseId(int purchaseId) {
        return shippingRepository.findByPurchaseId(purchaseId);
    }

    // 유저 이메일로 배송 목록 조회
    public List<Shipping> getShippingListByUserEmail(String userEmail) {
        return shippingRepository.findByPurchaseUserEmail(userEmail);
    }
}