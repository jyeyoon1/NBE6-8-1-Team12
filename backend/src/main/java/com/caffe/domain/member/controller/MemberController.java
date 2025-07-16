package com.caffe.domain.member.controller;


import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String loginPage(Authentication authentication) {
        // 이미 로그인된 상태인 경우, 상품 목록으로 리다이렉트
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/api/products/list";
        }
        return "/member/view_login";
    }


    // 모든 회원 조회
    @GetMapping
    public List<Member> getAllMembers() {
        List<Member> members = memberService.findAll();

        return members;
    }

    // ID로 회원 조회
    @GetMapping("/{id}")
    public Optional<Member> getMemberById(@PathVariable Integer id) {
        Optional<Member> member = memberService.findById(id);

        return member;
    }
}
