package com.caffe.domain.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentUpdateDto(
        @NotNull int paymentOptionId,
        @NotNull String paymentInfo,
        @Positive int amount
) {
}
