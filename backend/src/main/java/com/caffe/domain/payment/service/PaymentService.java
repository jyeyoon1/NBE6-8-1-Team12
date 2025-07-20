package com.caffe.domain.payment.service;

import com.caffe.domain.payment.dto.PaymentDetailOptionDto;
import com.caffe.domain.payment.dto.PaymentOptionDto;
import com.caffe.domain.payment.dto.PaymentOptionsDto;
import com.caffe.domain.payment.entity.Payment;
import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.constant.PaymentOptionType;
import com.caffe.domain.payment.repository.PaymentOptionRepository;
import com.caffe.domain.payment.repository.PaymentRepository;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.global.component.MockPaymentGatewayClient;
import com.caffe.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentOptionRepository paymentOptionRepository;
    private final MockPaymentGatewayClient paymentGatewayClient;

    public List<PaymentDetailOptionDto> getTopLevelPaymentOptions() {
        return paymentOptionRepository.findByTypeOrderBySortSeq(PaymentOptionType.TOP_LEVEL)
                .stream()
                .map(PaymentDetailOptionDto::new)
                .collect(Collectors.toList());
    }

    public List<PaymentDetailOptionDto> getDetailPaymentOptions(int parentId) {
        return paymentOptionRepository.findByParentIdOrderBySortSeq(parentId)
                .stream()
                .map(PaymentDetailOptionDto::new)
                .collect(Collectors.toList());
    }

    public List<PaymentOptionsDto> getAllPaymentOptions() {
        List<PaymentOption> paymentOptions = paymentOptionRepository.findAllChildrenWithOptions();
        Map<PaymentOption, List<PaymentOption>> groupByType = paymentOptions.stream()
                .collect(Collectors.groupingBy(PaymentOption::getParent));
        return groupByType.entrySet().stream()
                .map(entry -> {
                    String parentName = entry.getKey().getName();
                    List<PaymentOptionDto> children = entry.getValue().stream()
                            .map(PaymentOptionDto::new)
                            .collect(Collectors.toList());
                    return new PaymentOptionsDto(parentName, children);
                })
                .collect(Collectors.toList());
    }

    public List<Payment> getAll(){
        return paymentRepository.findAll();
    }
    public Page<Payment> getAllPage(int page, int size, String sortField, String sortOrder) {
        Pageable pageable;

        if (StringUtils.hasText(sortField)) {
            Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort sort = Sort.by(direction, sortField);
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }

        return paymentRepository.findAll(pageable);
    }

    public PaymentOption getPaymentOption(int paymentOptionId) {
        return paymentOptionRepository.findById(paymentOptionId).orElseThrow(() -> new ResourceNotFoundException("404-2","%d 에 해당하는 결제 옵션을 찾을 수 없습니다.".formatted(paymentOptionId)));
    }

    public Payment findById(int id) {
        return paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("404-1", "%d에 해당하는 결제 정보를 찾을 수 없습니다.".formatted(id)));
    }

    public Payment save(Purchase purchase, PaymentOption paymentOption, int amount) {
        Payment payment = new Payment(amount, purchase, paymentOption);
        return paymentRepository.save(payment);
    }

    public Payment request(Payment payment, String paymentInfo) {
        payment.updatePaymentInfo(paymentInfo);
        boolean isSuccess = paymentGatewayClient.charge(payment.getPaymentOption().getParent().getName(), payment.getPaymentOption().getName(), payment.getPaymentInfo(), payment.getAmount());
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
        return paymentRepository.save(payment);
    }

    public Payment findLatest() {
        return paymentRepository.findFirstByOrderByIdDesc().orElseThrow(() -> new ResourceNotFoundException("404-1", "최근 결제 정보가 없습니다."));
    }
}
