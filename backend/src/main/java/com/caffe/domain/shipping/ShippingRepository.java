package com.caffe.domain.shipping;

import com.caffe.domain.shipping.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ShippingRepository extends JpaRepository<Shipping, Integer> {

    // 구매 번호의 배송 정보 확인
    Optional<Shipping> findByPurchaseId(int purchaseId);

    // 이 이메일 가진 사람이 시킨 배송 목록 가져오기
    List<Shipping> findByPurchaseUserEmail(String userEmail);
}
