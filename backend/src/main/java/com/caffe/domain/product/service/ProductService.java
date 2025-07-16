package com.caffe.domain.product.service;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

        // 상품 전체조회
        public List<Product> getAllProducts() {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                throw new NoSuchElementException("등록된 상품이 없습니다.");
            }
            return products;
        }


        //상품 단건조회
        public Product getProductById(int id) {
            return productRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(id + "번 상품을 찾을 수 없습니다."));
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
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(id + "번 상품을 찾을 수 없습니다."));
            productRepository.delete(product);
        }


    }

