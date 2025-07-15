package com.caffe.domain.payment.service;

import com.caffe.domain.payment.dto.PaymentOptionDto;
import com.caffe.domain.payment.entity.PaymentOptionType;
import com.caffe.domain.payment.repository.PaymentOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentOptionService {
    private final PaymentOptionRepository paymentOptionRepository;

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
}
