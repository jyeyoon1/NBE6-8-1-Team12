package com.caffe.domain.product.controller;

import com.caffe.domain.product.dto.request.ProductCreateRequest;
import com.caffe.domain.product.dto.request.ProductUpdateRequest;
import com.caffe.domain.product.dto.response.ProductDetailResponse;
import com.caffe.domain.product.dto.response.ProductSummaryResponse;
import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import com.caffe.global.dto.PageResponseDto;
import com.caffe.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//상품 관리 REST API 컨트롤러

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "ProductApiController", description = "상품 관리 API")
public class ProductApiController {

    private final ProductService productService;

    //상품 다건 조회 API
    @GetMapping
    @Operation(summary = "상품 목록 조회")
    public ResponseEntity<RsData<PageResponseDto<ProductSummaryResponse>>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);

        Page<ProductSummaryResponse> productSummaryPage = products.map(ProductSummaryResponse::new);

        PageResponseDto<ProductSummaryResponse> response = new PageResponseDto<>(productSummaryPage);

        return ResponseEntity.ok(new RsData<>("200-0", "상품 목록 조회 성공", response));
    }

    //상품 단건 조회 API (상세 정보)
    @GetMapping("/{id}")
    @Operation(summary = "상품 단건 조회")
    public ResponseEntity<RsData<ProductDetailResponse>> getProduct(@PathVariable int id) {
        try {
            Product product = productService.getProductById(id);
            ProductDetailResponse response = new ProductDetailResponse(product);
            return ResponseEntity.ok(new RsData<>("200-1", "상품 조회 성공", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RsData<>("400-1", "상품을 찾을 수 없습니다."));
        }
    }

    // 상품 등록 API
    @PostMapping
    @Operation(summary = "상품 등록")
    public ResponseEntity<RsData<ProductDetailResponse>> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        Product savedProduct = productService.saveProduct(request.toEntity());
        ProductDetailResponse response = new ProductDetailResponse(savedProduct);
        return ResponseEntity.ok(new RsData<>("200-2", "상품이 성공적으로 등록되었습니다.", response));
    }

    // 상품 수정 API
    @PutMapping("/{id}")
    @Operation(summary = "상품 수정")
    public ResponseEntity<RsData<ProductDetailResponse>> updateProduct(@PathVariable int id, @Valid @RequestBody ProductUpdateRequest request) {
        Product product = productService.getProductById(id);

        // 상품 정보 업데이트
        product.updateProductInfo(
                request.productName(),
                request.price(),
                request.totalQuantity(),
                request.description(),
                request.imageUrl()
        );

        Product updatedProduct = productService.updateProduct(product);
        ProductDetailResponse response = new ProductDetailResponse(updatedProduct);
        return ResponseEntity.ok(new RsData<>("200-3", "상품이 성공적으로 수정되었습니다.", response));
    }

    // 상품 삭제 API
    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제")
    public ResponseEntity<RsData<Void>> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new RsData<>("200-4", "상품이 성공적으로 삭제되었습니다."));
    }

    // 상품 등록 폼 정보 조회 API
    @GetMapping("/form")
    @Operation(summary = "상품 등록 폼 정보 조회")
    public ResponseEntity<RsData<ProductCreateRequest>> getAddFormDefaults() {
        ProductCreateRequest emptyForm = ProductCreateRequest.empty();
        return ResponseEntity.ok(new RsData<>("200-3", "상품 등록 폼 정보 조회", emptyForm));
    }
}
