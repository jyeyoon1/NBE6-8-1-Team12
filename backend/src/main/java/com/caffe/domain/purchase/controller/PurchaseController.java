package com.caffe.domain.purchase.controller;

import com.caffe.domain.payment.dto.PaymentOptionDto;
import com.caffe.domain.payment.service.PaymentOptionService;
import com.caffe.domain.purchase.dto.PurchaseInfoDto;
import com.caffe.domain.purchase.dto.PurchasePageReqBody;
import com.caffe.domain.purchase.dto.PurchasePageResBody;
import com.caffe.domain.purchase.service.PurchaseService;
import com.caffe.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final PaymentOptionService paymentOptionService;

    @GetMapping("/checkout")
    public PurchasePageResBody showPurchasePage(
            @RequestParam int productId,
            @RequestParam int quantity
    ) {
        PurchaseInfoDto purchasePageInfo = purchaseService.getOrderPageInfo(productId, quantity);
        List<PaymentOptionDto> topLevelPaymentOptions = paymentOptionService.getTopLevelPaymentOptions();

        return new PurchasePageResBody(purchasePageInfo, topLevelPaymentOptions);
    }

    @PostMapping("/checkout")
    public RsData<Void> createPurchase(
            @Valid @RequestBody PurchasePageReqBody reqBody
    ) {
        purchaseService.createPurchase(reqBody);

        // 임시
        return new RsData<>(
                "201",
                "주문이 완료되었습니다."
        );
    }
}
