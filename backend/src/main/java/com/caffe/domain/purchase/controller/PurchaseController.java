package com.caffe.domain.purchase.controller;

import com.caffe.domain.payment.dto.PaymentOptionDto;
import com.caffe.domain.payment.service.PaymentService;
import com.caffe.domain.purchase.dto.PurchaseInfoDto;
import com.caffe.domain.purchase.dto.PurchasePageResBody;
import com.caffe.domain.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final PaymentService paymentService;

    @GetMapping("/checkout")
    public PurchasePageResBody showOrderPage(
            @RequestParam int productId,
            @RequestParam int quantity
    ) {
        PurchaseInfoDto orderPageInfo = purchaseService.getOrderPageInfo(productId, quantity);
        List<PaymentOptionDto> topLevelPaymentOptions = paymentService.getTopLevelPaymentOptions();

        return new PurchasePageResBody(orderPageInfo, topLevelPaymentOptions);
    }

}
