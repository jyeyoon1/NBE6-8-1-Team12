package com.caffe.domain.purchase.controller;

import com.caffe.domain.payment.dto.PaymentOptionDto;
import com.caffe.domain.payment.service.PaymentService;
import com.caffe.domain.purchase.dto.req.CartItemReqBody;
import com.caffe.domain.purchase.dto.req.PurchasePageReqBody;
import com.caffe.domain.purchase.dto.req.PurchaserReqBody;
import com.caffe.domain.purchase.dto.res.*;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.service.PurchaseService;
import com.caffe.global.dto.PageResponseDto;
import com.caffe.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchases")
@Tag(name = "PurchaseController", description = "Api 주문 컨트롤러")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PaymentService paymentService;

    @GetMapping("/list")
    @Operation(summary = "주문 목록 조회")
    public List<PurchaseAdmDto> getPurchases() {
        return purchaseService.getPurchases();
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회 - page")
    public PageResponseDto<PurchaseAdmDto> getPurchases(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<PurchaseAdmDto> paging = purchaseService.getPurchasesByPage(page, size);
        return new PageResponseDto<>(paging);
    }

    @PostMapping("/lookup")
    @Operation(
            summary = "주문번호, 이메일로 주문 존재 확인",
            description = "주문번호와 이메일을 사용하여 특정 주문이 시스템에 존재하는지 확인합니다."
    )
    public RsData<PurchaseLookupResBody> checkPurchaseExists(
            @Valid @RequestBody PurchaserReqBody reqBody
    ) {
        Purchase purchase = purchaseService.getPurchaseByIdAndUserEmail(reqBody.purchaseId(), reqBody.userEmail());

        return new RsData<>(
                "200",
                "주문이 확인되었습니다.",
                new PurchaseLookupResBody(purchase)
        );
    }

    @PostMapping("/lookup/detail")
    @Operation(
            summary = "주문 상세 조회",
            description = "주문번호와 이메일을 사용하여 특정 주문의 모든 상세 정보(주문 상품, 배송 정보 등)를 조회합니다."
    )
    public RsData<PurchaseDetailDto> getPurchase(
            @Valid @RequestBody PurchaserReqBody reqBody
    ) {
        PurchaseDetailDto purchaseDetail = purchaseService.getPurchaseDetail(reqBody);

        return new RsData<>(
                "200",
                "주문을 성공적으로 조회했습니다.",
                purchaseDetail
        );
    }

    @PostMapping("/purchaseInfo")
    @Operation(
            summary = "주문 작성 페이지 정보 조회",
            description = "결제 전 주문서 작성 페이지에 표시될 상품 목록 정보와 사용 가능한 결제 수단 목록을 조회합니다."
    )
    public PurchasePageResBody showPurchasePage(
            @Valid @RequestBody List<CartItemReqBody> reqBodyList
    ) {
        List<PurchaseItemInfoDto> purchaseItems = purchaseService.getPurchaseItemsInfo(reqBodyList);
        List<PaymentOptionDto> topLevelPaymentOptions = paymentService.getTopLevelPaymentOptions();

        return new PurchasePageResBody(purchaseItems, topLevelPaymentOptions);
    }

    @PostMapping("/checkout")
    @Operation(
            summary = "최종 주문 요청",
            description = "사용자가 선택한 상품들로 주문을 생성하고, 결제를 요청합니다."
    )
    public RsData<PurchaseCheckoutResBody> createPurchase(
            @Valid @RequestBody PurchasePageReqBody reqBody
    ) {
        return new RsData<>(
                "201",
                "주문이 성공적으로 완료되었습니다.",
                purchaseService.createPurchase(reqBody)
        );
    }
}
