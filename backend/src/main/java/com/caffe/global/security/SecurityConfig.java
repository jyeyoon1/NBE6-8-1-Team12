package com.caffe.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                //CORS 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                //CSRF  보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                //같은 도메인페이지에서만 이 페이지의 프레임을 표시
                .headers(
                        headers -> headers
                                .frameOptions(
                                        HeadersConfigurer.FrameOptionsConfig::sameOrigin
                                ))
                //HTTP Basic 인증 방식 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                //세션 관리 정책 설정 : STATELESS (세션을 사용하지 않음)
                //세션 관리 정책 설정: 세션 사용
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1) // 최대 세션 수
                        .maxSessionsPreventsLogin(false) // 새 로그인 시 기존 세션 무효화
                )
                //요청 경로별 인가 설정
                .authorizeHttpRequests(auth -> auth
                        // 로그인 페이지, 정적 리소스는 허용
                        .requestMatchers("/", "/api/member/login", "/api/member/status", "/css/**", "/js/**","/h2-console/**","/apidoc/**","/swagger-ui/**").permitAll()
                        // 상품 관련 페이지는 인증 필요
                        .requestMatchers("/api/products/**").authenticated()
                        .anyRequest().permitAll()  // 그 외 요청은 모두 승인(필요 시 변경)
                );
        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //허용할 Origin 주소
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        //허용할 HTTP METHOD
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        //허용할 헤더
        configuration.setAllowedHeaders(List.of("*"));
        //세션 쿠키를 위해 추가
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //모든 경로에 대해 위 설정 적용
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 비밀번호 암호화 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 직접 로그인 인증에 사용
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

}
