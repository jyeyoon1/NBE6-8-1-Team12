package com.caffe.domain.payment.controller;

import com.caffe.domain.payment.dto.*;
import com.caffe.domain.payment.entity.Payment;
import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.constant.PaymentStatus;
import com.caffe.domain.payment.service.PaymentService;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.service.PurchaseService;
import com.caffe.global.dto.PageResponseDto;
import com.caffe.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@Tag(name = "ApiV1PaymentController", description = "API 결제 컨트롤러")
public class ApiV1PaymentController {
    private final PaymentService paymentService;
    private final PurchaseService purchaseService;

    @GetMapping("/list")
    @Transactional(readOnly = true)
    @Operation(summary = "다건조회")
    public List<PaymentResponseDto> getAllPayments() {
        List<Payment> payments = paymentService.getAll();
        return payments.stream().map(PaymentResponseDto::new).toList();
    }

    @GetMapping()
    @Transactional(readOnly = true)
    @Operation(summary = "다건조회 - Page")
    public PageResponseDto<PaymentResponseDto> getAllPayments(@RequestParam(value = "page", defaultValue = "0")int page, @RequestParam(value = "size", defaultValue = "10")int size, @RequestParam(value = "sortField", defaultValue = "")String sortField, @RequestParam(value = "sortOrder", defaultValue = "")String sortOrder) {
        Page<Payment> paging = paymentService.getAllPage(page, size, sortField, sortOrder);
        return new PageResponseDto<>(paging.map(PaymentResponseDto::new));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "단건조회")
    public PaymentResponseDto getPayment(@PathVariable int id) {
        Payment payment = paymentService.findById(id);
        return new PaymentResponseDto(payment);
    }

    @PostMapping()
    @Transactional
    @Operation(summary = "결제 생성")
    public RsData<PaymentRequestResponseDto> request(@Valid @RequestBody PaymentRequestDto paymentRequestDto) {

        Purchase purchase = purchaseService.getPurchaseById(paymentRequestDto.purchaseId());
        PaymentOption paymentOption = paymentService.getPaymentOption(paymentRequestDto.paymentOptionId());
        if (paymentOption.getParent() == null) return new RsData<>("400-2", "잘못된 결제 옵션입니다. 상위 카테고리가 존재하지 않습니다.");
        Payment payment = paymentService.save(purchase, paymentOption, paymentRequestDto.amount());

        return new RsData<>("201-1", "결제번호 %d 가 생성되었습니다.".formatted(payment.getId()), new PaymentRequestResponseDto(payment));
    }

    @PostMapping("/{id}/execute") //post or fetch
    @Transactional
    @Operation(summary = "결제 요청")
    public RsData<Void> request(@Valid @RequestBody PaymentExecuteDto paymentExecuteDto, @PathVariable int id) {

        Payment payment = paymentService.findById(id);
        if(payment.getStatus()== PaymentStatus.SUCCESS) {
            return new RsData<>("409-1", "결제번호 %d 는 이미 결제되었습니다.".formatted(payment.getId()));
        }
        // 서비스 계층에서 결제 로직 처리
        payment = paymentService.request(payment, paymentExecuteDto.paymentInfo());

        return new RsData<>(payment.getStatus()== PaymentStatus.SUCCESS? "200-1":"503-1", "주문번호 %d의 결제가 ".formatted(payment.getPurchase().getId())+(payment.getStatus()== PaymentStatus.SUCCESS? "성공했습니다.":"실패했습니다."));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "결제 삭제")
    public RsData<Void> delete(@PathVariable int id) {
        Payment payment = paymentService.findById(id);
        paymentService.delete(payment);
        return new RsData<>("200-1", "결제번호 %d 가 삭제되었습니다.".formatted(id));
    }

    @PutMapping("/{id}/cancel") //post or fetch
    @Transactional
    @Operation(summary = "결제 취소")
    public RsData<Void> cancel(@PathVariable int id) {
        Payment payment = paymentService.findById(id);
        paymentService.cancel(payment);
        return new RsData<>("200-1", "결제번호 %d 가 취소되었습니다.".formatted(id));
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "결제 방법 수정")
    public RsData<PaymentUpdateResponseDto> update(@PathVariable int id, @Valid @RequestBody PaymentUpdateDto paymentUpdateDto) {
        Payment payment = paymentService.findById(id);
        PaymentOption paymentOption = paymentService.getPaymentOption(paymentUpdateDto.paymentOptionId());
        if (paymentOption.getParent() == null) return new RsData<>("400-2", "잘못된 결제 옵션입니다. 상위 카테고리가 존재하지 않습니다.");
        Payment updatedPayment = paymentService.changePayment(payment, paymentOption, paymentUpdateDto.paymentInfo(), paymentUpdateDto.amount());
        return new RsData<>("200-1", "결제번호 %d 가 수정되었습니다.".formatted(updatedPayment.getId()), new PaymentUpdateResponseDto(updatedPayment));
    }

    @GetMapping("/options/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "결제 방법 조회")
    public List<PaymentOptionDto> getDetailPaymentOptions(@PathVariable int id) {
        return paymentService.getDetailPaymentOptions(id);
    }
}