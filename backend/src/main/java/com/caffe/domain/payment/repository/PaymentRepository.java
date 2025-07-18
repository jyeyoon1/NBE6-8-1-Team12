package com.caffe.domain.payment.repository;

import com.caffe.domain.payment.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findById(int id);
    Optional<Payment> findFirstByOrderByIdDesc();
    Page<Payment> findAll(Pageable pageable);
}
