package com.caffe.domain.shipping.service;

import com.caffe.domain.purchase.dto.req.ReceiverReqDto;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.repository.PurchaseRepository;
import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.constant.ShippingStatus;
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

    // 배송 생성 (ReceiverReqDto 기반)
    public Shipping createShipping(ReceiverReqDto receiver, Purchase purchase) {
        Shipping shipping = Shipping.builder()
                .address(receiver.address())
                .postcode(receiver.postcode())
                .contactNumber(receiver.phoneNumber())
                .contactName(receiver.name())
                .carrier("CJ대한통운")
                .status(ShippingStatus.TEMPORARY)
                .purchase(purchase)
                .build();

        return shippingRepository.save(shipping);
    }


    public Shipping saveShipping(Shipping shipping) {
        return shippingRepository.save(shipping);
    }

    // 구매 ID로 구매 조회
    public Purchase getPurchaseById(int purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 구매를 찾을 수 없습니다."));
    }

    // 구매 ID로 배송 조회
    public Optional<Shipping> getShippingByPurchaseId(int purchaseId) {
        return shippingRepository.findByPurchaseId(purchaseId);
    }

   // 유저 이메일로 배송 목록 조회
   public List<Shipping> getShippingListByUserEmail(String email) {
       return shippingRepository.findByEmail(email);
   }


    // 유저 이메일로 구매 내역 조회
    public List<Purchase> getPurchasesByUserEmail(String userEmail) {
        return purchaseRepository.findAllByUserEmail(userEmail);
    }
}
