package com.caffe.domain.purchase.dto;

import jakarta.validation.constraints.NotBlank;

public record ReceiverReqDto(
        @NotBlank
        String name,
        @NotBlank
        String phoneNumber,
        // 주소 : 주소 api 연동 전 수기 입력 (배송 담당자 논의 필요)
        // 우편주소 여부
        @NotBlank
        String address
        /*@NotBlank
        String postcode*/
) {
}
