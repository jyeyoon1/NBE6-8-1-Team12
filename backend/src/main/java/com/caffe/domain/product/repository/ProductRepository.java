package com.caffe.domain.product.repository;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsByProductName(String productName);

    Product findByProductName(String productName);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByStatusIn(List<ProductStatus> statuses, Pageable pageable);
}