package com.caffe.domain.shipping.controller;

import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.service.PurchaseService;
import com.caffe.domain.shipping.dto.ShippingDto;
import com.caffe.domain.shipping.dto.ShippingResDto;
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
    private final PurchaseService purchaseService;

    // 이메일로 해당 유저의 구매 목록 조회
    @GetMapping("/purchases/{email}")
    public List<Purchase> getPurchasesByEmail(@PathVariable("email") String userEmail) {
        return shippingService.getPurchasesByUserEmail(userEmail);
    }

    // 배송 생성 (ShippingDto -> Shipping)
    @PostMapping
    public List<ShippingResDto> createShippingsForAllPurchases(@RequestBody ShippingDto dto) {
        List<Purchase> purchases = shippingService.getPurchasesByUserEmail(dto.getEmail());

        List<Shipping> shippings = purchases.stream().map(purchase ->
                Shipping.builder()
                        .email(dto.getEmail())
                        .address(dto.getAddress())
                        .postcode(dto.getPostcode())
                        .contactName(dto.getContactName())
                        .contactNumber(dto.getContactNumber())
                        .carrier("CJ대한통운")
                        .status(dto.getStatus())
                        .purchase(purchase)
                        .build()
        ).toList();

        shippings.forEach(shippingService::saveShipping);

        return shippings.stream()
                .map(ShippingResDto::new)
                .toList();
    }


    @GetMapping("/{email}")
    public List<ShippingResDto> getShippingByEmail(@PathVariable String email) {
        return shippingService.getShippingListByUserEmail(email).stream()
                .map(ShippingResDto::new)
                .toList();
    }

}

