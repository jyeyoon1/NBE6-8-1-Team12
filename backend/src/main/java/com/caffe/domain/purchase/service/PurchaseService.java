package com.caffe.domain.purchase.service;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import com.caffe.domain.purchase.constant.PurchaseStatus;
import com.caffe.domain.purchase.dto.req.*;
import com.caffe.domain.purchase.dto.res.*;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.entity.PurchaseItem;
import com.caffe.domain.purchase.repository.PurchaseItemRepository;
import com.caffe.domain.purchase.repository.PurchaseRepository;
import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.service.ShippingService;
import com.caffe.global.exception.BusinessLogicException;
import com.caffe.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ProductService productService;
    private final ShippingService shippingService;

    public List<PurchaseAdmDto> getPurchases() {
        return purchaseRepository
                .findAllByOrderByCreateDateDesc()
                .stream()
                .map(PurchaseAdmDto::new)
                .collect(Collectors.toList());
    }

    public Page<PurchaseAdmDto> getPurchasesByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return purchaseRepository
                .findAllByOrderByCreateDateDesc(pageable)
                .map(PurchaseAdmDto::new);
    }

    public Purchase getPurchaseById(int purchaseId) {
        return purchaseRepository
                .findById(purchaseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("404-1", "%s번 주문을 찾을 수 없습니다.".formatted(purchaseId))
                );
    }

    public Purchase getPurchaseByIdAndUserEmail(int id, String userEmail) {
        return purchaseRepository
                .findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("404-2", "이메일과 주문 번호를 확인해주십시오.")
                );
    }

    public PurchaseDetailDto getPurchaseDetail(PurchaserReqBody reqBody) {
        // 주문 정보
        Purchase purchase = getPurchaseByIdAndUserEmail(reqBody.purchaseId(), reqBody.userEmail());
        PurchaseDto purchaseDto = new PurchaseDto(purchase);

        // 구매 제품 목록
        List<PurchaseItemDetailDto> purchaseItems = purchase
                .getPurchaseItems()
                .stream()
                .map(item -> new PurchaseItemDetailDto(item, item.getProduct()))
                .toList();

        // 배송 정보
        Shipping shipping = shippingService
                .getShippingByPurchaseId(reqBody.purchaseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("404-3", "%s번 배송을 찾을 수 없습니다.".formatted(purchase.getId()))
                );
        ReceiverResDto receiverResDto = new ReceiverResDto(shipping);

        return new PurchaseDetailDto(purchaseDto, purchaseItems, receiverResDto);
    }

    public List<PurchaseItemInfoDto> getPurchaseItemsInfo(List<CartItemReqBody> reqBodyList) {
        return reqBodyList
                .stream()
                .map(item -> getPurchaseItemInfo(item.productId(), item.quantity()))
                .collect(Collectors.toList());
    }

    public PurchaseItemInfoDto getPurchaseItemInfo(int productId, int quantity) {
        Product product = productService.getProductById(productId);
        int totalPrice = calculateTotalPrice(product.getPrice(), quantity);

        return new PurchaseItemInfoDto(product, quantity, totalPrice);
    }

    public int calculateTotalPrice(int price, int quantity) {
        return price * quantity;
    }

    // 결제 전 임시 저장 기능 - 결제 완료 시 주문 완료 상태로 변경
    @Transactional
    public PurchaseCheckoutResBody createPurchase(PurchasePageReqBody reqBody) {
        // 구매자
        PurchaserReqDto purchaser = reqBody.purchaser();
        String userEmail = purchaser.email();
        // 구매 제품
        List<PurchaseItemReqDto> purchaseItems = reqBody.purchaseItems();
        // 배송정보
        ReceiverReqDto receiver = reqBody.receiver();

        // 구매 제품 재고 확인
        for (PurchaseItemReqDto itemReqDto : purchaseItems) {
            Product product = productService.getProductById(itemReqDto.productId());
            if(!product.hasStock(itemReqDto.quantity())) {
                throw new BusinessLogicException("409-1", "%s의 재고가 부족합니다.".formatted(product.getProductName()));
            }
        }

        // Purchase 저장
        Purchase purchase = new Purchase(userEmail);
        // PurchaseItem 저장
        for (PurchaseItemReqDto itemReqDto : purchaseItems) {
            Product product = productService.getProductById(itemReqDto.productId());
            PurchaseItem purchaseItem = new PurchaseItem(itemReqDto, product);
            purchase.addPurchaseItem(purchaseItem);
        }
        purchaseRepository.save(purchase);

        // Shipping 저장
        shippingService.createShipping(receiver, purchase);

        return new PurchaseCheckoutResBody(purchase, reqBody.paymentOptionId());
    }

    @Transactional
    public void completePurchase(Purchase purchase) {
        try {
            purchase.completePurchase();

            for (PurchaseItem purchaseItem : purchase.getPurchaseItems()) {
                Product product = purchaseItem.getProduct();
                product.decreaseStock(purchaseItem.getQuantity());
            }
        } catch (IllegalArgumentException e) {
            failPurchase(purchase);
            throw new BusinessLogicException("400-1", "주문 완료가 불가합니다.");
        }
    }

    @Transactional
    public void cancelPurchase(Purchase purchase) {
        try {
            purchase.cancelPurchase();

            for (PurchaseItem purchaseItem : purchase.getPurchaseItems()) {
                Product product = purchaseItem.getProduct();
                product.restoreStock(purchaseItem.getQuantity());
            }
        } catch (IllegalArgumentException e) {
            failPurchase(purchase);
            throw new BusinessLogicException("400-2", "주문 취소가 불가합니다.");
        }
    }

    @Transactional
    public void failPurchase(Purchase purchase) {
        if(purchase.getStatus().equals(PurchaseStatus.PURCHASED)) {
            for (PurchaseItem purchaseItem : purchase.getPurchaseItems()) {
                Product product = purchaseItem.getProduct();
                product.restoreStock(purchaseItem.getQuantity());
            }
        }
        purchase.failPurchase();
    }
}
