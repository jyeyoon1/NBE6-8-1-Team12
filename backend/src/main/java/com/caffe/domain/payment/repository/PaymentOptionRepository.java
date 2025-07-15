package com.caffe.domain.payment.repository;

import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.entity.PaymentOptionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentOptionRepository extends JpaRepository<PaymentOption, Integer> {
    List<PaymentOption> findByTypeOrderBySortSeq(PaymentOptionType type);
    List<PaymentOption> findByParentIdOrderBySortSeq(int parentId);
}
