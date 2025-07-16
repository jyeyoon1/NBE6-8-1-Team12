package com.caffe.domain.payment.controller;

import com.caffe.domain.payment.dto.PaymentDto;
import com.caffe.domain.payment.dto.PaymentOptionDto;
import com.caffe.domain.payment.entity.Payment;
import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.service.PaymentService;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.service.PurchaseService;
import com.caffe.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@Tag(name = "ApiV1PaymentController", description = "API 결제 컨트롤러")
public class PaymentController {
    private final PaymentService paymentService;
    private final PurchaseService purchaseService;

    @GetMapping()
    @Transactional(readOnly = true)
    @Operation(summary = "다건조회")
    public List<PaymentDto.PaymentResponseDto> getAll() {
        List<Payment> payments = paymentService.getAll();
        return payments.stream().map(PaymentDto.PaymentResponseDto::new).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "단건조회")
    public PaymentDto.PaymentResponseDto getOne(@PathVariable int id) {
        Payment payment = paymentService.findById(id).get();
        return new PaymentDto.PaymentResponseDto(payment);
    }

    @PostMapping()
    @Transactional
    @Operation(summary = "결제 요청")
    public RsData<PaymentDto.PaymentResponseDto> request(@Valid @RequestBody PaymentDto.PaymentRequestDto paymentRequestDto) {

        Purchase purchase = purchaseService.getPurchaseById(paymentRequestDto.purchaseId());
        Optional<PaymentOption> paymentOption = paymentService.getPaymentOption(paymentRequestDto.paymentOptionId());
        if(paymentOption.isEmpty()) {
            return new RsData<>("404-1","지원하지 않는 결제방식입니다.");
        }
        // 서비스 계층에서 결제 로직 처리
        Payment payment = paymentService.request(purchase, paymentOption.get(), paymentRequestDto.paymentInfo(), paymentRequestDto.amount());

        return new RsData<>(payment.getStatus()=='S'? "201-1":"503-1", "주문번호 %d의 결제가 ".formatted(purchase.getId())+(payment.getStatus()=='S'? "성공했습니다.":"실패했습니다."), new PaymentDto.PaymentResponseDto(payment));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "결제 삭제")
    public RsData<Void> cancel(@PathVariable int id) {
        Payment payment = paymentService.findById(id).get();
        paymentService.cancel(payment);
        return new RsData<>("200-1", "결제번호 %d 가 삭제되었습니다.".formatted(id));
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "결제 방법 수정")
    public RsData<PaymentDto.PaymentResponseDto> update(@PathVariable int id, @Valid @RequestBody PaymentDto.PaymentUpdateDto paymentUpdateDto) {
        Optional<Payment> payment = paymentService.findById(id);
        if(payment.isEmpty()) {
            return new RsData<>("404-1","결제정보를 찾을 수 없습니다.",null);
        }
        Optional<PaymentOption> paymentOption = paymentService.getPaymentOption(paymentUpdateDto.paymentOptionId());
        if(paymentOption.isEmpty()) {
            return new RsData<>("400-1","잘못된 결제방법입니다.",null);
        }
        Payment updatedPayment = paymentService.changePayment(payment.get(), paymentOption.get(), paymentUpdateDto.paymentInfo(), paymentUpdateDto.amount());
        return new RsData<>(
                updatedPayment.getStatus()=='S'? "201-1":"503-1",
                "주문번호 %d의 결제가 ".formatted(updatedPayment.getPurchase().getId())+(updatedPayment.getStatus()=='S'? "성공했습니다.":"실패했습니다."),
                new PaymentDto.PaymentResponseDto(updatedPayment));
    }

    @GetMapping("/options/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "결제 방법 조회")
    public List<PaymentOptionDto> getDetailPaymentOptions(@PathVariable int id) {
        return paymentService.getDetailPaymentOptions(id);
    }
}