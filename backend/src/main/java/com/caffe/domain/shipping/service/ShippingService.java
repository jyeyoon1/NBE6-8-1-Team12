package com.caffe.domain.shipping.service;

import com.caffe.domain.purchase.dto.req.ReceiverReqDto;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.repository.PurchaseRepository;
import com.caffe.domain.shipping.dto.ShippingResDto;
import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.constant.ShippingStatus;
import com.caffe.domain.shipping.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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
                .purchase(purchase)
                .email(receiver.email())
                .createDate(LocalDateTime.now())
                .build();

        shipping.assignInitialStatus();

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

    // 이메일로 배송 목록 조회 (DTO)
    public List<ShippingResDto> getShippingListByUserEmailDto(String email) {
        return shippingRepository.findByEmail(email).stream()
                .map(ShippingResDto::new)
                .toList();
    }



    // 유저 이메일로 구매 내역 조회
    public List<Purchase> getPurchasesByUserEmail(String userEmail) {
        return purchaseRepository.findAllByUserEmail(userEmail);
    }

    public List<ShippingResDto> getAllShippings() {
        return shippingRepository.findAll()
                .stream()
                .map(ShippingResDto::new)
                .toList();
    }

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void updateShippingStatusBasedOnOrderTime() {
        List<Shipping> shippings = shippingRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Shipping shipping : shippings) {
            LocalTime orderTime = shipping.getCreateDate().toLocalTime();
            ShippingStatus currentStatus = shipping.getStatus();

            if (currentStatus == ShippingStatus.BEFORE_DELIVERY) {
                if (orderTime.isBefore(LocalTime.of(9, 0)) || orderTime.isAfter(LocalTime.of(13, 59))) {
                    shipping.updateStatus(ShippingStatus.DELIVERING, now);
                    log.info("주문 ID {} 상태를 DELIVERING으로 업데이트", shipping.getId());
                }
            }
        }
    }


    // 주문일자에 따라 초기 배송 상태 결정
    public static ShippingStatus determineInitialStatus(LocalDateTime createDate) {
        LocalTime time = createDate.toLocalTime();

        if (time.isBefore(LocalTime.of(9, 0))) {
            return ShippingStatus.BEFORE_DELIVERY; // 배송전
        } else if (time.isBefore(LocalTime.of(14, 0))) {
            return ShippingStatus.DELIVERING; // 배송
        } else {
            return ShippingStatus.BEFORE_DELIVERY; // 배송전
        }
    }

}
