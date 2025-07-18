package com.caffe.domain.member.dto.request;

import com.caffe.domain.member.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 멤버 수정 요청 DTO
 */
public record MemberUpdateRequest(
        @NotBlank String password,
        @NotBlank String username,
        @NotNull Role role
) {}