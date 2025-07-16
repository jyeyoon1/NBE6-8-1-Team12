package com.caffe.domain.purchase.service;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import com.caffe.domain.purchase.dto.*;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.entity.PurchaseItem;
import com.caffe.domain.purchase.entity.PurchaseStatus;
import com.caffe.domain.purchase.repository.PurchaseItemRepository;
import com.caffe.domain.purchase.repository.PurchaseRepository;
import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.entity.ShippingStatus;
import com.caffe.domain.shipping.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ProductService productService;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ShippingRepository shippingRepository;


    public Purchase getPurchaseById(int purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException(purchaseId + "번 주문을 찾을 수 없습니다."));
    }

    public PurchaseInfoDto getOrderPageInfo(int productId, int quantity) {
        Product product = productService.getProductById(productId);
        double totalPrice = calculateTotalPrice(product.getPrice(), quantity);

        return new PurchaseInfoDto(product, quantity, totalPrice);
    }

    public double calculateTotalPrice(double price, int quantity) {
        return price * quantity;
    }

    // setter -> 생성자로 변경 논의 필요
    // 결제 전 임시 저장 - 결제 취소 버튼 클릭 시 롤백 필요
    @Transactional
    public void createPurchase(PurchasePageReqBody reqBody) {
        // 구매자
        PurchaserReqDto purchaser = reqBody.purchaser();
        String userEmail = purchaser.email();

        // 구매 제품
        PurchaseReqDto purchaseInfo = reqBody.purchase();

        // 제품
        Product product = productService.getProductById(purchaseInfo.productId());

        // 받는이
        ReceiverReqDto receiver = reqBody.receiver();


        // Purchase 저장
        Purchase purchase = new Purchase();
        purchase.setUserEmail(userEmail);
        purchase.setTotalPrice(purchaseInfo.totalPrice());
        purchase.setStatus(PurchaseStatus.TEMPORARY); // 결제 후 주문완료 상태로 변경

        // PurchaseItem 저장
        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setPrice(purchaseInfo.price());
        purchaseItem.setQuantity(purchaseInfo.quantity());
        purchaseItem.setProduct(product);

        purchase.addPurchaseItem(purchaseItem);
        purchaseRepository.save(purchase);

        // 배송 담당자 논의 필요
        // Shipping 저장 -> 해당 서비스의 저장 로직 필요 (매개변수 : Purchase, ReceiverDto)
        Shipping shipping = new Shipping();
        shipping.setPurchase(purchase);
        shipping.setAddress(receiver.address());
        shipping.setContactNumber(receiver.phoneNumber());
        shipping.setContactName(receiver.name());
        shipping.setStatus(ShippingStatus.BEFORE_DELIVERY); // 임시 상태 필요 -> 결제 후 배송전으로 변경
        shippingRepository.save(shipping);

        // 회원 담당자 논의 필요
        // User 저장 - PurchaserReqDto

        // 결제 담당자 논의 필요
        // 결제 연동 로직
    }
}
