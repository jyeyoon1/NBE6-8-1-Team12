package com.caffe.domain.product.controller;


import com.caffe.domain.product.dto.ProductDTO;
import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import com.caffe.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping("/add")
    @Valid
    public ResponseEntity<RsData<Void>> addProduct(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO.toEntity());
        return ResponseEntity.ok(new RsData<>("200-1", "상품이 성공적으로 등록되었습니다."));
    }

    // 상품 수정
    @PutMapping("/{id}")
    @Valid
    public ResponseEntity<RsData<Product>> updateProduct(@PathVariable int id, @RequestBody ProductDTO dto) {
        Product product = productService.getProductById(id);

        product.setProductName(dto.getProductName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setTotalQuantity(dto.getTotalQuantity());

        Product updateProduct = productService.updateProduct(product);
        return ResponseEntity.ok(new RsData<>("200-2", "상품이 성공적으로 수정되었습니다.", updateProduct));
    }

    // GET 모든 상품 조회
    @GetMapping("/list")
    public String listProducts(Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("products", productList);
        return "product/list_product";
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
        return "product/add_product"; // templates/product/add_product.html
    }


    // 상품 수정 폼 보여주기
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product/edit_product";
    }

    // DELETE 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<RsData<Void>> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new RsData<>("200-3", "상품이 성공적으로 삭제되었습니다."));
    }
}
