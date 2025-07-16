package com.caffe.domain.purchase.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PurchaserReqDto(
        @NotBlank
        @Email(message = "올바른 이메일 형식을 입력하세요.")
        String email,
        // 비회원 주문 처리 방식 결정 필요 (회원 담당자 논의 필요)
        // - 단순 임시 객체로만 사용하는지, 임시 user 엔티티로 저장하는지 결정 필요
        // - 비밀번호 포함 시: 저장 방식 검토
        // - 비밀번호 없이 처리 시: 이메일, 주문번호로만 주문 이력 조회
        /*@NotBlank
        String password,*/
        @NotBlank
        String name
) {
}
