package com.caffe.domain.product.controller;

import com.caffe.domain.product.dto.ProductDTO;
import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import com.caffe.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//상품 관리 REST API 컨트롤러

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "ProductApiController", description = "상품 관리 API 컨트롤러")
public class ProductApiController {

    private final ProductService productService;

   //상품 다건 조회 API

    @GetMapping
    @Operation(summary = "상품 다건 조회")
    public ResponseEntity<RsData<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new RsData<>("200-0", "상품 목록 조회 성공", products));
    }

    //상품 단건 조회 API
    @GetMapping("/{id}")
    @Operation(summary = "상품 단건 조회")
    public ResponseEntity<RsData<Product>> getProduct(@PathVariable int id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new RsData<>("200-1", "상품 조회 성공", product));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new RsData<>("400-1", "상품을 찾을 수 없습니다."));
        }
    }

    // 상품 등록 API
    @PostMapping
    @Operation(summary = "상품 등록")
    public ResponseEntity<RsData<Void>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO.toEntity());
        return ResponseEntity.ok(new RsData<>("200-2", "상품이 성공적으로 등록되었습니다."));
    }

    // 상품 수정 API
    @PutMapping("/{id}")
    @Operation(summary = "상품 수정")
    public ResponseEntity<RsData<Product>> updateProduct(@PathVariable int id, @Valid @RequestBody ProductDTO dto) {
        Product product = productService.getProductById(id);
        
        // 상품 정보 업데이트
        product.updateProductInfo(
                dto.getProductName(),
                dto.getPrice(),
                dto.getTotalQuantity(),
                dto.getDescription(),
                dto.getImageUrl()
        );

        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(new RsData<>("200-3", "상품이 성공적으로 수정되었습니다.", updatedProduct));
    }

    // 상품 삭제 API
    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제")
    public ResponseEntity<RsData<Void>> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new RsData<>("200-4", "상품이 성공적으로 삭제되었습니다."));
    }
}
