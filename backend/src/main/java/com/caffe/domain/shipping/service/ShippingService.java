package com.caffe.domain.shipping.service;

import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.repository.PurchaseRepository;
import com.caffe.domain.shipping.dto.ShippingDto;
import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.caffe.domain.shipping.entity.ShippingStatus;

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
     - 프론트에서 받은 ShippingDto 기반으로 Shipping 엔티티 생성
     - 배송 상태는 기본적으로 BEFORE_DELIVERY가 설정됨
     - 이후 저장 후 반환(DELIVERING)
    */
    public Shipping createShipping(ShippingDto dto) {
        Purchase purchase = purchaseRepository.findById(dto.getPurchaseId())
                .orElseThrow(() -> new IllegalArgumentException("해당 구매를 찾을 수 없습니다."));

        Shipping shipping = new Shipping();
        shipping.setPurchase(purchase);
        shipping.setAddress(dto.getAddress());
        shipping.setContactNumber("010-0000-0000"); // TODO: 임시 값, 유저 정보에서 받아오도록 변경 예정
        shipping.setContactName("홍길동");           // TODO: 임시 값, 유저 정보에서 받아오도록 변경 예정
        shipping.setCarrier("CJ대한통운");           // TODO: 향후 carrier 선택 기능 있으면 수정
        shipping.setStatus(dto.getStatus() != null ? dto.getStatus() : ShippingStatus.BEFORE_DELIVERY);

        return shippingRepository.save(shipping);
    }


    // 구매 ID로 배송 조회
    public Optional<Shipping> getShippingByPurchaseId(int purchaseId) {
        return shippingRepository.findByPurchaseId(purchaseId);
    }

    // 유저 이메일로 배송 목록 조회
    public List<Shipping> getShippingListByUserEmail(String userEmail) {
        return shippingRepository.findByPurchaseUserEmail(userEmail);
    }

    /*
     - 유저 이메일로 가장 최근 구매 내역 조회
     - 이메일 기준으로 가장 최신 Purchase 1건 조회
     - 주로 배송 생성 시 어떤 구매건인지 식별용
    */
    public Purchase getLatestPurchaseByUserEmail(String userEmail) {
        return purchaseRepository.findTopByUserEmailOrderByCreateDateDesc(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("구매 내역이 없습니다."));
    }
}
