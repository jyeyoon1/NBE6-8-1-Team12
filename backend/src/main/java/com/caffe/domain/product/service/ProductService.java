package com.caffe.domain.product.service;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


        public Product getProductById(int id) {
            return productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        }
    }

