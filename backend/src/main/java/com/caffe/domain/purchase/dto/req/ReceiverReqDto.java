package com.caffe.domain.purchase.dto.req;

import jakarta.validation.constraints.NotBlank;

public record ReceiverReqDto(
        @NotBlank
        String name,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String address, // 주소
        @NotBlank
        int postcode, // 우편번호
        @NotBlank
        String email,
        @NotBlank
        String status // 배송 상태
) {
}
