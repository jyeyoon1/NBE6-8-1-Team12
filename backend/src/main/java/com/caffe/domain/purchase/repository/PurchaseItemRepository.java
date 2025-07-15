package com.caffe.domain.purchase.repository;

import com.caffe.domain.purchase.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Integer> {
}
