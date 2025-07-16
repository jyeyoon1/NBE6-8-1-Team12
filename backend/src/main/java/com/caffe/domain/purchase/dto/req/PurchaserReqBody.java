package com.caffe.domain.purchase.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaserReqBody(
        @NotBlank
        @Email
        String userEmail,

        @NotNull
        @Positive
        int purchaseId
) {
}
