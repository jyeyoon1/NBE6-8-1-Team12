package com.caffe.domain.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequestDto(
        @NotNull int purchaseId,
        @NotNull int paymentOptionId,
        @Positive int amount
) {
}
