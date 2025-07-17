package com.caffe.domain.payment.service;

import com.caffe.domain.payment.dto.PaymentOptionDto;
import com.caffe.domain.payment.entity.Payment;
import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.entity.PaymentOptionType;
import com.caffe.domain.payment.repository.PaymentOptionRepository;
import com.caffe.domain.payment.repository.PaymentRepository;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.global.component.MockPaymentGatewayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentOptionRepository paymentOptionRepository;
    private final MockPaymentGatewayClient paymentGatewayClient;

    public List<PaymentOptionDto> getTopLevelPaymentOptions() {
        return paymentOptionRepository.findByTypeOrderBySortSeq(PaymentOptionType.TOP_LEVEL)
                .stream()
                .map(PaymentOptionDto::new)
                .collect(Collectors.toList());
    }

    public List<PaymentOptionDto> getDetailPaymentOptions(int parentId) {
        return paymentOptionRepository.findByParentIdOrderBySortSeq(parentId)
                .stream()
                .map(PaymentOptionDto::new)
                .collect(Collectors.toList());
    }

    public List<Payment> getAll(){
        return paymentRepository.findAll();
    }

    public Optional<PaymentOption> getPaymentOption(int paymentOptionId) {
        return paymentOptionRepository.findById(paymentOptionId);
    }

    public Optional<Payment> findById(int id) {
        return paymentRepository.findById(id);
    }

    public Payment request(Purchase purchase, PaymentOption paymentOption, String paymentInfo, int amount) {
        Payment payment = new Payment(paymentInfo, amount, purchase, paymentOption);
        boolean isSuccess = paymentGatewayClient.charge(paymentOption.getType().toString(), paymentOption.getName(), payment.getPaymentInfo(), payment.getAmount());
        payment.isSuccess(isSuccess);
        return paymentRepository.save(payment);
    }

    public void delete(Payment payment) {
        paymentRepository.delete(payment);
    }

    public void cancel(Payment payment) {
        payment.cancel();
        paymentRepository.save(payment);
    }

    public Payment changePayment(Payment payment, PaymentOption paymentOption, String paymentInfo, int amount) {
        payment.updatePayment(paymentOption, paymentInfo, amount==0? payment.getAmount(): amount);
        boolean isSuccess = paymentGatewayClient.charge(paymentOption.getType().toString(), paymentOption.getName(), payment.getPaymentInfo(), payment.getAmount());
        payment.isSuccess(isSuccess);
        return paymentRepository.save(payment);
    }

    public Optional<Payment> findLatest() {
        return paymentRepository.findFirstByOrderByIdDesc();
    }
}
