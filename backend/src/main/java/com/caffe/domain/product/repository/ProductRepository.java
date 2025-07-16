package com.caffe.domain.product.repository;

import com.caffe.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    boolean existsByProductName(String productName);

    Product findByProductName(String productName);
}