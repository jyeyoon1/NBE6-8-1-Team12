package com.caffe.global.initdata;

import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.entity.Role;
import com.caffe.domain.member.repository.MemberRepository;
import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.entity.PaymentOptionType;
import com.caffe.domain.payment.repository.PaymentOptionRepository;
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
    private final PaymentOptionRepository paymentOptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationContext applicationContext;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            BaseInitData self = applicationContext.getBean(BaseInitData.class);
            self.newAdmin();
            self.initProducts();
            self.initPaymentOptions();
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
        product1.setTotalQuantity(100);
        product1.setDescription("고소한 맛이 특징인 콜롬비아 나리뇨 커피입니다.");
        product1.setImageUrl("https://i.imgur.com/HKOFQYa.jpeg");

        Product product2 = new Product();
        product2.setProductName("Ethiopian Yirgacheffe");
        product2.setPrice(6500);
        product2.setTotalQuantity(50);
        product2.setDescription("플로럴 향이 풍부한 에티오피아 예가체프 커피입니다.");
        product2.setImageUrl("https://m.media-amazon.com/images/I/71LN-graGzL._UF894%2C1000_QL80_.jpg");

        productRepository.saveAll(List.of(product1, product2));
        System.out.println("기본 상품 2개 생성 완료");
    }

    private void initPaymentOptions() {
        if (paymentOptionRepository.count() > 0) return;
        PaymentOption paymentOption1 = new PaymentOption();
        paymentOption1.setType(PaymentOptionType.TOP_LEVEL);
        paymentOption1.setCode("0001");
        paymentOption1.setName("BANK");
        paymentOption1.setSortSeq(1);
        PaymentOption paymentOption2 = new PaymentOption();
        paymentOption2.setType(PaymentOptionType.TOP_LEVEL);
        paymentOption2.setCode("0002");
        paymentOption2.setName("CARD");
        paymentOption2.setSortSeq(2);
        PaymentOption paymentOption3 = new PaymentOption();
        paymentOption3.setType(PaymentOptionType.TOP_LEVEL);
        paymentOption3.setCode("0003");
        paymentOption3.setName("PG");
        paymentOption3.setSortSeq(3);
        PaymentOption paymentOption11 = new PaymentOption();
        paymentOption11.setType(PaymentOptionType.DETAIL);
        paymentOption11.setCode("000101");
        paymentOption11.setName("NH");
        paymentOption11.setSortSeq(1);
        paymentOption11.setParent(paymentOption1);
        PaymentOption paymentOption12 = new PaymentOption();
        paymentOption12.setType(PaymentOptionType.DETAIL);
        paymentOption12.setCode("000102");
        paymentOption12.setName("HN");
        paymentOption12.setSortSeq(2);
        paymentOption12.setParent(paymentOption1);
        PaymentOption paymentOption21 = new PaymentOption();
        paymentOption21.setType(PaymentOptionType.DETAIL);
        paymentOption21.setCode("000201");
        paymentOption21.setName("WR");
        paymentOption21.setSortSeq(1);
        paymentOption21.setParent(paymentOption2);
        PaymentOption paymentOption22 = new PaymentOption();
        paymentOption22.setType(PaymentOptionType.DETAIL);
        paymentOption22.setCode("000202");
        paymentOption22.setName("HD");
        paymentOption22.setSortSeq(2);
        paymentOption22.setParent(paymentOption2);
        PaymentOption paymentOption31 = new PaymentOption();
        paymentOption31.setType(PaymentOptionType.DETAIL);
        paymentOption31.setCode("000301");
        paymentOption31.setName("KAKAO");
        paymentOption31.setSortSeq(1);
        paymentOption31.setParent(paymentOption3);
        PaymentOption paymentOption32 = new PaymentOption();
        paymentOption32.setType(PaymentOptionType.DETAIL);
        paymentOption32.setCode("000302");
        paymentOption32.setName("Toss");
        paymentOption32.setSortSeq(2);
        paymentOption32.setParent(paymentOption3);

        paymentOptionRepository.saveAll(List.of(paymentOption1, paymentOption2, paymentOption3, paymentOption11, paymentOption12, paymentOption21, paymentOption22,paymentOption31, paymentOption32));
        System.out.println("결제 옵션 데이터 9개  생성 완료");

    }
}