package com.caffe.domain.payment.dto;

import jakarta.validation.constraints.NotNull;

public record PaymentExecuteRequestDto(
        @NotNull String paymentInfo
) {
}