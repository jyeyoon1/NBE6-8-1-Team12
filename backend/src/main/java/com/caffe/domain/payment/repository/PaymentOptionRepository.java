package com.caffe.domain.payment.repository;

import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.constant.PaymentOptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentOptionRepository extends JpaRepository<PaymentOption, Integer> {
    List<PaymentOption> findByTypeOrderBySortSeq(PaymentOptionType type);
    List<PaymentOption> findByParentIdOrderBySortSeq(int parentId);

    //N+1 문제를 방지하기 위해 fetch욥션 사용
    @Query("SELECT po FROM PaymentOption po JOIN FETCH po.parent WHERE po.parent IS NOT NULL ORDER BY po.parent.sortSeq, po.sortSeq")
    List<PaymentOption> findAllChildrenWithOptions();
}
