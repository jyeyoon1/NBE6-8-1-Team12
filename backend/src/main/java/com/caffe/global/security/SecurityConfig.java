package com.caffe.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
                //HTTP Basic 인증 방식 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                //Form 기반 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                //세션 관리 정책 설정 : STATELESS (세션을 사용ㅎ지 않음)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //요청 경로별 인가 설정
                .authorizeHttpRequests(auth -> auth
                        //모든 요청 허용
                        .requestMatchers("/**").permitAll()
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

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //모든 경로에 대해 위 설정 적용
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
