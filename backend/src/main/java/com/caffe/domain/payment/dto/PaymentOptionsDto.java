package com.caffe.domain.payment.dto;

import java.util.List;

public record PaymentOptionsDto (
    String optionType,
    List<PaymentOptionDto> options
){
}
