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

        // 상품 전체조회
        public List<Product> getAllProducts() {
            return productRepository.findAll();
        }


        //상품 단건조회
        public Product getProductById(int id) {
            return productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException(id + "번 상품을 찾을 수 없습니다."));
        }

        // 상품 등록
        public Product saveProduct(Product product) {
            return productRepository.save(product);
        }

        // 상품 수정
        public Product updateProduct(Product product) {
            return productRepository.save(product);
        }

        // 상품 삭제
        public void deleteProduct(int id) {
            productRepository.deleteById(id);
        }


    }

