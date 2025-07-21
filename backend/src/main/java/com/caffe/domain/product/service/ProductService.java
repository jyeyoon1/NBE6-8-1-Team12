package com.caffe.domain.product.service;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.entity.ProductStatus;
import com.caffe.domain.product.repository.ProductRepository;
import com.caffe.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final SecurityUtil securityUtil;

        // 상품 전체조회 (관리자 권한 체크)
        public Page<Product> getAllProducts(Pageable pageable, Authentication authentication) {
            boolean isAdmin = securityUtil.isAdmin(authentication);

            if (isAdmin) {
                // 관리자: 모든 상품 조회
                return productRepository.findAll(pageable);
            } else {
                // 일반 사용자: 판매중 또는 재고없음 상품만 조회
                return productRepository.findByStatusIn(
                        List.of(ProductStatus.ON_SALE, ProductStatus.OUT_OF_STOCK),
                        pageable
                );
            }
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
                throw new IllegalArgumentException("상품 설명은 1000자 이하여야 합니다.");
            }

            // 7. 이미지 URL 길이 제한 (500자 제한)
            if (product.getImageUrl() != null && product.getImageUrl().length() > 500) {
                throw new IllegalArgumentException("이미지 URL은 500자 이하여야 합니다.");
            }

            // 8. ID 초기화 (새로운 상품 등록시 ID를 null로 설정하여 자동 생성되도록)
            try {
                return productRepository.save(product);
            } catch (Exception e) {
                if (e.getMessage().contains("Unique index or primary key violation") || 
                    e.getMessage().contains("duplicate key")) {
                    throw new IllegalArgumentException("상품 등록 중 중복 오류가 발생했습니다. 다시 시도해주세요.");
                }
                throw e;
            }
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
                throw new IllegalArgumentException("상품 설명은 1000자 이하여야 합니다.");
            }

            // 7. 이미지 URL 길이 제한 (500자 제한)
            if (product.getImageUrl() != null && product.getImageUrl().length() > 500) {
                throw new IllegalArgumentException("이미지 URL은 500자 이하여야 합니다.");
            }

            return productRepository.save(product);
        }

        // 상품 재고수량만 수정
        public Product updateProductStockOnly(int productId, int newQuantity) {
            if (newQuantity < 0) {
                throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSuchElementException(productId + "번 상품을 찾을 수 없습니다."));

            product.updateStock(newQuantity);

            // [자동 상태 변경 로직 추가]
            if (newQuantity == 0) {
                product.updateStatus(ProductStatus.OUT_OF_STOCK);
            } else {
                // 재고가 다시 들어오면, OUT_OF_STOCK인 경우 ON_SALE로 자동 복구
                if (product.getStatus() == ProductStatus.OUT_OF_STOCK) {
                    product.updateStatus(ProductStatus.ON_SALE);
                }
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

