package com.caffe.global.initdata;

import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.entity.Role;
import com.caffe.domain.member.repository.MemberRepository;
import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationContext applicationContext;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            BaseInitData self = applicationContext.getBean(BaseInitData.class);
            self.newAdmin();
            self.initProducts();
        };
    }

    private void newAdmin() {
        if (memberRepository.count() > 0) return;
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setPassword(passwordEncoder.encode("test"));
        member.setRole(Role.ADMIN);
        memberRepository.save(member);
        System.out.println("초기 멤버 생성 완료");
    }

    private void initProducts() {
        if (productRepository.count() > 0) return;

        Product product1 = new Product();
        product1.setProductName("Columbia Nariñó");
        product1.setPrice(5000);
        product1.setTotal_quantity(100);
        product1.setDescription("고소한 맛이 특징인 콜롬비아 나리뇨 커피입니다.");
        product1.setImage_url("https://i.imgur.com/HKOFQYa.jpeg");

        Product product2 = new Product();
        product2.setProductName("Ethiopian Yirgacheffe");
        product2.setPrice(6500);
        product2.setTotal_quantity(50);
        product2.setDescription("플로럴 향이 풍부한 에티오피아 예가체프 커피입니다.");
        product2.setImage_url("https://i.imgur.com/7tZ8J3K.jpeg");

        productRepository.saveAll(List.of(product1, product2));
        System.out.println("기본 상품 2개 생성 완료");
    }
}