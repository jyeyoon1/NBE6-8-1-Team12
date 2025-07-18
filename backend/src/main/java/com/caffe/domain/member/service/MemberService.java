package com.caffe.domain.member.service;

import com.caffe.domain.member.dto.request.MemberUpdateRequest;
import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 전체 회원 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Member findById(Integer id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + "번 회원을 찾을 수 없습니다."));
    }

    // 회원 등록
    public Member save(Member member) {

        // 1. null 체크
        if (member == null) {
            throw new IllegalArgumentException("회원 정보가 없습니다.");
        }

        // 2. 이메일 중복 검사
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다: " + member.getEmail());
        }

        // 3. 사용자명 유효성 검사
        if (member.getUsername() == null || member.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("사용자명을 입력해주세요.");
        }

        // 4. 비밀번호 유효성 검사
        if (member.getPassword() == null || member.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        return memberRepository.save(member);
    }

    // 회원 수정
    public Member update(Integer id, MemberUpdateRequest request) {

        // 1. 기존 회원 조회 (없는 경우 예외 발생)
        Member member = findById(id);

        // 2. 비밀번호 유효성 검사
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        // 3. 사용자명 유효성 검사
        if (request.username() == null || request.username().isBlank()) {
            throw new IllegalArgumentException("사용자명은 필수입니다.");
        }

        // 4. 회원 정보 업데이트
        member.updateInfo(request.password(), request.username(), request.role());

        return memberRepository.save(member);
    }

    // 회원 삭제
    public void delete(Integer id) {

        // 1. 기존 회원 조회 (없는 경우 예외 발생)
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + "번 회원을 찾을 수 없습니다."));

        // 2. 삭제
        memberRepository.delete(member);
    }
}