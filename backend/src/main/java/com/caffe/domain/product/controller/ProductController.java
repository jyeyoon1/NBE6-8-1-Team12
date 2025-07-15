package com.caffe.domain.product.controller;


import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

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

    // 상품 추가 처리
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/api/products/list"; // 저장 후 상품 목록으로 이동
    }


    // POST 상품 등록
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // PUT 상품 수정
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
        return productService.updateProduct(product);
    }

    // DELETE 상품 삭제
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }
}
