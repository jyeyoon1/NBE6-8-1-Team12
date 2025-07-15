package com.caffe.global.initdata;

import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;
    private final MemberRepository memberRepository;


    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.newAdmin();
        };
    }

    private void newAdmin() {
        if (memberRepository.count() > 0) return;

        // Member 객체 생성 및 저장
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setPassword("test");
        // 필요한 필드 세팅

        memberRepository.save(member);
        System.out.println("초기 멤버 생성 완료");
    }

}
