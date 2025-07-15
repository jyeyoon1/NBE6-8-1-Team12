package com.caffe.domain.member.service;

import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // ID로 회원 찾기
    public Optional<Member> findById(Integer id) {
        return memberRepository.findById(id);
    }

    // 모든 회원 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }


}