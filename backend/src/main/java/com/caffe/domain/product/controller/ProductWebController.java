package com.caffe.domain.product.controller;

import com.caffe.domain.product.dto.ProductDTO;
import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

// 상품 관리 Web Controller (뷰 렌더링 전용)
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductWebController {

    private final ProductService productService;

    // 상품 목록 조회
    @GetMapping({"", "/list"})
    public String listProducts(Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("products", productList);
        return "product/list_product";
    }

    // 상품 상세 조회
    @GetMapping("/detail/{id}")
    public String getProductById(@PathVariable int id, Model model) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            return "product/detail_product";
        } catch (NoSuchElementException e) {
            model.addAttribute("errorMessage", "ID " + id + "번 상품을 찾을 수 없습니다.");
            return "error/product_not_found";
        }
    }

    // 상품 추가 폼 보여주기
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/add_product";
    }

    // 상품 수정 폼 보여주기
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        try{
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            return "product/edit_product";
        } catch (NoSuchElementException e) {
            model.addAttribute("errorMessage", "수정할 상품을 찾을 수 없습니다");
            return "error/product_not_found";
        }
    }

    // 폼 기반 상품 등록 처리
    @PostMapping("/new")
    public String addProduct(@ModelAttribute ProductDTO productDTO, Model model) {
        try {
            productService.saveProduct(productDTO.toEntity());
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록에 실패했습니다.");
            model.addAttribute("product", productDTO);
            return "product/add_product";
        }
    }

    // 폼 기반 상품 수정 처리
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable int id, @ModelAttribute ProductDTO productDTO, Model model) {
        try {
            Product product = productService.getProductById(id);
            
            // 상품 정보 업데이트
            product.setProductName(productDTO.getProductName());
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setImageUrl(productDTO.getImageUrl());
            product.setTotalQuantity(productDTO.getTotalQuantity());

            productService.updateProduct(product);
            return "redirect:/products/detail/" + id;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정에 실패했습니다.");
            model.addAttribute("product", productDTO);
            return "product/edit_product";
        }
    }

    // 폼 기반 상품 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id, Model model) {
        try {
            productService.deleteProduct(id);
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 삭제에 실패했습니다.");
            return "redirect:/products/detail/" + id;
        }
    }
}
