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

    @GetMapping("/purchases/{email}")
    public List<Purchase> getPurchasesByEmail(@PathVariable("email") String userEmail) {
        return shippingService.getPurchasesByUserEmail(userEmail);
    }

    @PostMapping
    public List<ShippingResDto> createShippingsForAllPurchases(@RequestBody ShippingDto dto) {
        List<Purchase> purchases = shippingService.getPurchasesByUserEmail(dto.getEmail());

        List<Shipping> shippings = purchases.stream().map(purchase -> {
            Shipping shipping = Shipping.builder()
                    .email(dto.getEmail())
                    .address(dto.getAddress())
                    .postcode(dto.getPostcode())
                    .contactName(dto.getContactName())
                    .contactNumber(dto.getContactNumber())
                    .carrier("CJ대한통운")
                    .purchase(purchase)
                    .build();

            // 상태 자동 설정
            shipping.assignInitialStatus();

            return shippingService.saveShipping(shipping);
        }).toList();

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

    @GetMapping
    public List<ShippingResDto> getAllShippings() {
        return shippingService.getAllShippings();
    }
}
