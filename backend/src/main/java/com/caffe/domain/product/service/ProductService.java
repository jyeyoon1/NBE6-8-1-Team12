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
            return productRepository.findAll();
        }


        //상품 단건조회
        public Product getProductById(int id) {
            return productRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(id + "번 상품을 찾을 수 없습니다."));
        }

        // 상품 등록
        public Product saveProduct(Product product) {

            //  1.null 체크
            if (product == null) {
                throw new IllegalArgumentException("상품 정보가 제공되지 않았습니다.");
            }

            // 2.상품명 유효성 검사
            if (product.getProductName() == null || product.getProductName().isEmpty()) {
                throw new IllegalArgumentException("상품명을 등록해주세요.");
            }

            // 3.가격 유효성 검사
            if (product.getPrice() <= 0) {
                throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
            }

            // 4.중복 상품명 검사
            if (productRepository.existsByProductName(product.getProductName())) {
                throw new IllegalArgumentException("이미 등록된 상품입니다." + product.getProductName() );
            }

            // 5.quantity 유효성 검사
            if (product.getTotalQuantity() < 0) {
                throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
            }

            // 6. 설명 길이 제한 (1000자 제한)
            if (product.getDescription() != null && product.getDescription().length() > 1000) {
                throw new IllegalArgumentException("상품 설명은 500자 이하여야 합니다.");
            }

            return productRepository.save(product);
        }

        // 상품 수정
        public Product updateProduct(Product product) {

            // 1. null 체크
            if (product == null) {
                throw new IllegalArgumentException("상품 정보가 없습니다.");
            }

            // 2. 상품 존재 여부 확인
            if (!productRepository.existsById(product.getId())) {
                throw new NoSuchElementException(product.getId() + "번 상품을 찾을 수 없습니다.");
            }

            // 3. 상품명 유효성 검사
            if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
                throw new IllegalArgumentException("상품명은 필수입니다.");
            }

            // 4. 가격 유효성 검사
            if (product.getPrice() < 0) {
                throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
            }

            // 5. 중복 상품명 체크 (자기 자신 제외)
            Product existingProduct = productRepository.findByProductName(product.getProductName());
            if (existingProduct != null && existingProduct.getId() != product.getId()) {
                throw new IllegalArgumentException("이미 존재하는 상품명입니다: " + product.getProductName());
            }

            // 6. quantity 유효성 검사
            if (product.getTotalQuantity() < 0) {
                throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
            }

            // 6. 설명 길이 제한 (1000자 제한)
            if (product.getDescription() != null && product.getDescription().length() > 1000) {
                throw new IllegalArgumentException("상품 설명은 500자 이하여야 합니다.");
            }

            return productRepository.save(product);
        }

        // 상품 삭제
        public void deleteProduct(int id) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(id + "번 상품을 찾을 수 없습니다."));
            productRepository.delete(product);
        }


    }

