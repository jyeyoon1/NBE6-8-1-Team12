package com.caffe.domain.member.dto.response;

import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.entity.Role;

/**
 * 멤버 상세 조회 응답 DTO (모든 정보 *비밀번호 제외)
 */
public record MemberResponse(
        int id,
        String email,
        String username,
        Role role
) {
    public MemberResponse(Member member) {
        this(member.getId(), member.getEmail(), member.getUsername(), member.getRole());
    }
}
