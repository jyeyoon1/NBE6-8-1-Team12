package com.caffe.domain.product.controller;

import com.caffe.domain.product.dto.request.ProductCreateRequest;
import com.caffe.domain.product.dto.request.ProductUpdateRequest;
import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
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
    public String listProducts(Pageable pageable, Model model) {
        Page<Product> productPage = productService.getAllProducts(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);
        return "product/list_product";
    }

    // GET ID로 단건 조회
    @GetMapping("/{id}")
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
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", ProductCreateRequest.empty());
        return "product/add_product";
    }

    // 상품 수정 폼 보여주기
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        try{
            Product product = productService.getProductById(id);
            // Entity를 UpdateRequest로 변환해서 폼에 전달
            ProductUpdateRequest request = new ProductUpdateRequest(product);
            model.addAttribute("product", request);
            return "product/edit_product";
        } catch (NoSuchElementException e) {
            model.addAttribute("errorMessage", "수정할 상품을 찾을 수 없습니다");
            return "error/product_not_found";
        }
    }

    // 폼 기반 상품 등록 처리
    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductCreateRequest request, Model model) {
        try {
            productService.saveProduct(request.toEntity());
            return "redirect:/products/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록에 실패했습니다.");
            model.addAttribute("product", request);
            return "product/add_product";
        }
    }

    // 폼 기반 상품 수정 처리
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable int id, @ModelAttribute ProductUpdateRequest request, Model model) {
        try {
            Product product = productService.getProductById(id);
            
            // 상품 정보 업데이트
            product.updateProductInfo(
                    request.productName(),
                    request.price(),
                    request.totalQuantity(),
                    request.description(),
                    request.imageUrl()
            );

            productService.updateProduct(product);
            return "redirect:/products/" + id;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정에 실패했습니다.");
            model.addAttribute("product", request);
            return "product/edit_product";
        }
    }

    // 폼 기반 상품 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id, Model model) {
        try {
            productService.deleteProduct(id);
            return "redirect:/products/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 삭제에 실패했습니다.");
            return "redirect:/products/" + id;
        }
    }
}
