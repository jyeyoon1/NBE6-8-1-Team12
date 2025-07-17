package com.caffe.domain.purchase.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReceiverReqDto(
        @NotBlank
        String name,

        @NotBlank
        String phoneNumber,

        @NotBlank
        String address, // 주소

        @NotNull
        @Positive
        int postcode, // 우편번호

        @NotBlank
        String email
) {
}
