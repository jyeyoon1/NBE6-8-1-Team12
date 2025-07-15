package com.caffe.domain.purchase.repository;

import com.caffe.domain.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    /*
     -특정 유저의 가장 최근 구매 내역 1건 조회
     - createDate 기준으로 가장 최근 구매를 가져온다.
     - 배송 등록 시 최신 구매에 연결할 때 사용된다.
     */
    Optional<Purchase> findTopByUserEmailOrderByCreateDateDesc(String userEmail);


}
