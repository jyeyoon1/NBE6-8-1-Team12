package com.caffe.domain.purchase.service;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.service.ProductService;
import com.caffe.domain.purchase.dto.req.*;
import com.caffe.domain.purchase.dto.res.*;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.entity.PurchaseItem;
import com.caffe.domain.purchase.repository.PurchaseItemRepository;
import com.caffe.domain.purchase.repository.PurchaseRepository;
import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ProductService productService;
    private final ShippingService shippingService;

    public Purchase getPurchaseById(int purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException(purchaseId + "번 주문을 찾을 수 없습니다."));
    }

    public Purchase getPurchaseByUserEmailAndPurchaseId(int id, String userEmail) {
        return purchaseRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new IllegalArgumentException("이메일과 주문 번호를 확인해주십시오."));
    }

    public PurchaseDetailDto getPurchaseDetail(PurchaserReqBody reqBody) {
        // 주문 정보
        Purchase purchase = getPurchaseByUserEmailAndPurchaseId(reqBody.purchaseId(), reqBody.userEmail());
        PurchaseDto purchaseDto = new PurchaseDto(purchase);

        // 임시 조치 -> 현재 상세페이지에서 구매하는 1:1 관계임. 추후 변경 예정 (아직 개발전)
        // 구매 제품 정보
        PurchaseItem purchaseItem = purchase.getPurchaseItems().get(0);
        PurchaseItemDetailDto purchaseItemDetailDto = new PurchaseItemDetailDto(purchaseItem, purchaseItem.getProduct());

        // 배송 정보
        Shipping shipping = shippingService.getShippingByPurchaseId(reqBody.purchaseId())
                .orElseThrow(() -> new IllegalArgumentException("배송을 찾을 수 없습니다."));
        ReceiverResDto receiverResDto = new ReceiverResDto(shipping);

        return new PurchaseDetailDto(purchaseDto, purchaseItemDetailDto, receiverResDto);
    }

    public PurchaseInfoDto getOrderPageInfo(int productId, int quantity) {
        Product product = productService.getProductById(productId);
        double totalPrice = calculateTotalPrice(product.getPrice(), quantity);

        return new PurchaseInfoDto(product, quantity, totalPrice);
    }

    public double calculateTotalPrice(double price, int quantity) {
        return price * quantity;
    }

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
        // 배송정보
        ReceiverReqDto receiver = reqBody.receiver();

        // Purchase 저장
        Purchase purchase = new Purchase(userEmail);
        // PurchaseItem 저장
        PurchaseItem purchaseItem = new PurchaseItem(purchaseInfo, product);
        purchase.addPurchaseItem(purchaseItem);
        purchaseRepository.save(purchase);

        // Shipping 저장
        shippingService.createShipping(receiver);

        // 결제 담당자 논의 필요
        // 결제 연동 로직
    }
}
