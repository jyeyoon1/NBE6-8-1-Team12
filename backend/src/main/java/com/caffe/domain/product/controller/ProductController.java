package com.caffe.domain.product.controller;


import com.caffe.domain.product.dto.ProductDTO;
import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // GET 모든 상품 조회
//    @GetMapping("/list")
//    public String listProducts(Model model) {
//        List<Product> productList = productService.getAllProducts();
//        model.addAttribute("products", productList);
//        return "product/list_product";
//    }

    // 상품 목록 조회 REST API 버전
    @GetMapping("/list")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // GET ID로 단건 조회
    @GetMapping("/{id}")
    public String getProductById(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product/detail_product";
    }

    // 상품 추가 폼 보여주기
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/add_product";
    }

    // 상품 추가 처리
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO.toEntity());
        return ResponseEntity.ok("상품이 성공적으로 추가되었습니다.");
    }


    // 상품 수정 폼 보여주기
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product/edit_product";
    }

    // 상품 수정 처리
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody ProductDTO dto) {
        Product product = productService.getProductById(id);

        product.setProductName(dto.getProductName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setTotalQuantity(dto.getTotalQuantity());

        productService.saveProduct(product);
        return ResponseEntity.ok().build();
    }


    // POST 상품 등록
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }



    // DELETE 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
