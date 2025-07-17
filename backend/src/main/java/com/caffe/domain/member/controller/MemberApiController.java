package com.caffe.domain.member.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberApiController {


    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(Map.of("message", "로그아웃 성공"));
    }

    // 현재 로그인 상태 확인용 API
    @GetMapping("/status")
    public ResponseEntity<?> getStatus(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok().body(Map.of(
                    "authenticated", true,
                    "username", authentication.getName()
            ));
        } else {
            return ResponseEntity.ok().body(Map.of(
                    "authenticated", false
            ));
        }
    }

}
