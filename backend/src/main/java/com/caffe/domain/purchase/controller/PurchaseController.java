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

    @GetMapping
    @Operation(summary = "주문 목록")
    public List<PurchaseAdmDto> getPurchases() {
        return purchaseService.getPurchases();
    }

    @GetMapping("/page")
    @Operation(summary = "주문 목록")
    public PageResponseDto<PurchaseAdmDto> getPurchases(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<PurchaseAdmDto> paging = purchaseService.getPurchasesByPage(page, size);
        return new PageResponseDto<>(paging);
    }

    @PostMapping("/lookup")
    @Operation(summary = "주문번호, 이메일로 주문 존재 확인")
    public RsData<PurchaseLookupResBody> checkPurchaseExists(
            @Valid @RequestBody PurchaserReqBody reqBody
    ) {
        Purchase purchase = purchaseService.getPurchaseByIdAndUserEmail(reqBody.purchaseId(), reqBody.userEmail());

        return new RsData<>(
                "200",
                "주문 존재 확인",
                new PurchaseLookupResBody(purchase)
        );
    }

    @PostMapping("/lookup/detail")
    @Operation(summary = "주문 조회")
    public RsData<PurchaseDetailDto> getPurchase(
            @Valid @RequestBody PurchaserReqBody reqBody
    ) {
        PurchaseDetailDto purchaseDetail = purchaseService.getPurchaseDetail(reqBody);

        return new RsData<>(
                "200",
                "주문 조회 성공",
                purchaseDetail
        );
    }

    @PostMapping("/purchaseInfo")
    @Operation(summary = "주문 화면 주문 정보 조회")
    public PurchasePageResBody showPurchasePage(
            @Valid @RequestBody List<CartItemReqBody> reqBodyList
    ) {
        List<PurchaseItemInfoDto> purchaseItems = purchaseService.getPurchaseItemsInfo(reqBodyList);
        List<PaymentOptionDto> topLevelPaymentOptions = paymentService.getTopLevelPaymentOptions();

        return new PurchasePageResBody(purchaseItems, topLevelPaymentOptions);
    }

    @PostMapping("/checkout")
    @Operation(summary = "주문")
    public RsData<PurchaseCheckoutResBody> createPurchase(
            @Valid @RequestBody PurchasePageReqBody reqBody
    ) {
        return new RsData<>(
                "201",
                "주문이 완료되었습니다.",
                purchaseService.createPurchase(reqBody)
        );
    }
}
