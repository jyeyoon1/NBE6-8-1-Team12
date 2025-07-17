package com.caffe.domain.shipping.controller;

import com.caffe.domain.purchase.dto.req.ReceiverReqDto;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.shipping.dto.ShippingDto;
import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shippings")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    /*
      - 배송 생성 API
      - 결제하기 버튼 클릭 시 호출
      - 프론트에서 받은 ShippingDto를 바탕으로 배송 생성
     */
    @PostMapping
    public Shipping createShipping(@RequestParam int purchaseId, @RequestBody ReceiverReqDto receiverReqDto) {
        Purchase purchase = shippingService.getPurchaseById(purchaseId);
        return shippingService.createShipping(purchase, receiverReqDto);
    }


    /*`
      - 최신 구매 내역 조회 API
      - 특정 이메일로 가장 최근에 생성된 구매 내역을 조회
      - 프론트에서 이메일 입력 후 blur 이벤트 시 호출
     */
    @GetMapping("/latest-purchase/{email}")
    public Purchase getLatestPurchase(@PathVariable("email") String userEmail) {
        return shippingService.getLatestPurchaseByUserEmail(userEmail);
    }

}
