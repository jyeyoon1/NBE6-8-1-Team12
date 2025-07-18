package com.caffe.domain.product.repository;

import com.caffe.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsByProductName(String productName);

    Product findByProductName(String productName);

    Page<Product> findAll(Pageable pageable);
}