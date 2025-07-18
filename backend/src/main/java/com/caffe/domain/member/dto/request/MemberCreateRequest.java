package com.caffe.domain.member.dto.request;

import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 멤버 생성 요청 DTO
 */
public record MemberCreateRequest(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String username,
        @NotNull Role role
) {
    public Member toEntity() {
        return new Member(email, password, username, role);
    }
}
