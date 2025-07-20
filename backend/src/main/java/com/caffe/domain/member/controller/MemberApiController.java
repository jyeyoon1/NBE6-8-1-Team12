package com.caffe.domain.member.controller;


import com.caffe.domain.member.dto.request.MemberCreateRequest;
import com.caffe.domain.member.dto.request.MemberUpdateRequest;
import com.caffe.domain.member.dto.response.MemberResponse;
import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.service.MemberService;
import com.caffe.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "MemberApiController", description = "회원 관리 API")
public class MemberApiController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    // 회원 목록 조회
    @GetMapping
    @Operation(summary = "회원 목록 조회")
    public ResponseEntity<RsData<List<MemberResponse>>> getAllMembers() {
        List<Member> members = memberService.findAll();
        List<MemberResponse> responses = members.stream().map(MemberResponse::new).toList();
        return ResponseEntity.ok(new RsData<>("200-0", "회원 목록 조회 성공", responses));
    }

    // 회원 단건 조회
    @GetMapping("/{id}")
    @Operation(summary = "회원 단건 조회")
    public ResponseEntity<RsData<MemberResponse>> getMember(@PathVariable int id) {
        try {
            Member member = memberService.findById(id);
            return ResponseEntity.ok(new RsData<>("200-1", "회원 조회 성공", new MemberResponse(member)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RsData<>("400-1", "회원을 찾을 수 없습니다."));
        }
    }

    // 회원 등록
    @PostMapping
    @Operation(summary = "회원 등록")
    public ResponseEntity<RsData<MemberResponse>> createMember(@Valid @RequestBody MemberCreateRequest request) {
        Member member = memberService.save(request.toEntity());
        return ResponseEntity.ok(new RsData<>("200-2", "회원 등록 성공", new MemberResponse(member)));
    }

    // 회원 수정
    @PutMapping("/{id}")
    @Operation(summary = "회원 수정")
    public ResponseEntity<RsData<MemberResponse>> updateMember(@PathVariable int id, @Valid @RequestBody MemberUpdateRequest request) {
        Member updated = memberService.update(id, request);
        return ResponseEntity.ok(new RsData<>("200-3", "회원 수정 성공", new MemberResponse(updated)));
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "회원 삭제")
    public ResponseEntity<RsData<Void>> deleteMember(@PathVariable int id) {
        memberService.delete(id);
        return ResponseEntity.ok(new RsData<>("200-4", "회원 삭제 성공"));
    }
    
    
    
    // 로그인 API
    @PostMapping("/login")
    @Operation(summary = "로그인 API")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password,
                                   HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션 생성 및 인증 정보 저장
            HttpSession session = request.getSession(true);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );

            System.out.println("로그인 성공 - 세션 ID: " + session.getId());
            System.out.println("인증된 사용자: " + authentication.getName());

            return ResponseEntity.ok().body(Map.of(
                    "message", "로그인 성공",
                    "username", authentication.getName(),
                    "sessionId", session.getId()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "아이디 또는 비밀번호가 틀렸습니다."
            ));
        }
    }

    // 로그아웃 API
    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(Map.of("message", "로그아웃 성공"));
    }

    // 로그인 상태 조회 API
    @GetMapping("/status")
    @Operation(summary = "로그인 상태 조회 API")
    public ResponseEntity<?> getStatus(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // 사용자의 권한 정보 가져오기
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

            return ResponseEntity.ok().body(Map.of(
                    "authenticated", true,
                    "username", authentication.getName(),
                    "isAdmin", isAdmin,
                    "role", isAdmin ? "ADMIN" : "USER"
            ));
        } else {
            return ResponseEntity.ok().body(Map.of(
                    "authenticated", false,
                    "isAdmin", false,
                    "role", "GUEST"
            ));
        }
    }


}
