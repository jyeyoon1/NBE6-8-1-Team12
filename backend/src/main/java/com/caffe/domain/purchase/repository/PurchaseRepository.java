package com.caffe.domain.purchase.repository;

import com.caffe.domain.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    /*
     -특정 유저의 가장 최근 구매 내역 1건 조회
     - createDate 기준으로 가장 최근 구매를 가져온다.
     - 배송 등록 시 최신 구매에 연결할 때 사용된다.
     */
    Optional<Purchase> findTopByUserEmailOrderByCreateDateDesc(String userEmail);

    // email을 기준으로 그 사람이 구매한 모든 Purchase를 가져올 때 사용
    List<Purchase> findAllByUserEmail(String userEmail);

    // 장바구니 기능 추가 후, 주문:주문 제품 여러개로 될 시 패치조인으로 변경 예정
    Optional<Purchase> findByIdAndUserEmail(int id, String email);
}
