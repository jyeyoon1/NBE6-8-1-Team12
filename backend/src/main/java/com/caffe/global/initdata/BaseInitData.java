package com.caffe.global.initdata;

import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.entity.Role;
import com.caffe.domain.member.repository.MemberRepository;
import com.caffe.domain.payment.constant.PaymentOptionType;
import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.repository.PaymentOptionRepository;
import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.entity.ProductStatus;
import com.caffe.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void newAdmin() {
        if (memberRepository.count() > 0) return;

        Member member = new Member(
                "test@test.com",
                passwordEncoder.encode("test"),
                "관리자",
                Role.ADMIN
        );

        memberRepository.save(member);
        System.out.println("초기 멤버 생성 완료");
    }

    @Transactional
    public void initProducts() {
        if (productRepository.count() > 0) return;

        Product product1 = new Product(
                "Columbia Nariñó",
                5000,
                100,
                "고소한 맛이 특징인 콜롬비아 나리뇨 커피입니다.",
                "https://i.imgur.com/HKOFQYa.jpeg",
                ProductStatus.ON_SALE    // 상태 추가
        );

        Product product2 = new Product(
                "Ethiopian Yirgacheffe",
                6500,
                50,
                "플로럴 향이 풍부한 에티오피아 예가체프 커피입니다.",
                "https://m.media-amazon.com/images/I/71LN-graGzL._UF894%2C1000_QL80_.jpg",
                ProductStatus.ON_SALE
        );

        Product product3 = new Product(
                "Sumatra Mandheling",
                6000,
                0,
                "묵직하고 깊은 바디감의 수마트라 만델링 커피입니다.",
                "https://images.unsplash.com/photo-1621971866884-4459f3b116d6",
                ProductStatus.OUT_OF_STOCK
        );

        Product product4 = new Product(
                "Guatemala Antigua",
                5500,
                20,
                "밸런스가 뛰어난 과테말라 안티구아 커피입니다.",
                "https://images.unsplash.com/photo-1621971667209-ff90c4b92146",
                ProductStatus.NOT_FOR_SALE
        );

        productRepository.saveAll(List.of(product1, product2, product3, product4));
        System.out.println("기본 상품 4개 생성 완료");
    }

    @Transactional
    public void initPaymentOptions() {
        if (paymentOptionRepository.count() > 0) return;
        PaymentOption paymentOption1 = new PaymentOption("BANK", PaymentOptionType.TOP_LEVEL, "0001", 1);
        PaymentOption paymentOption2 = new PaymentOption("CARD", PaymentOptionType.TOP_LEVEL, "0002", 2);
        PaymentOption paymentOption3 = new PaymentOption("PG", PaymentOptionType.TOP_LEVEL, "0003", 3);
        //PaymentOption paymentOption4 = new PaymentOption("통신사", PaymentOptionType.TOP_LEVEL, "0004", 4);
        PaymentOption paymentOption11 = new PaymentOption("농협은행" , PaymentOptionType.DETAIL, "0011", 3, paymentOption1);
        PaymentOption paymentOption12 = new PaymentOption("하나은행" , PaymentOptionType.DETAIL, "0012", 2, paymentOption1);
        PaymentOption paymentOption13 = new PaymentOption("신한은행" , PaymentOptionType.DETAIL, "0013", 1, paymentOption1);
        PaymentOption paymentOption14 = new PaymentOption("국민은행" , PaymentOptionType.DETAIL, "0014", 0, paymentOption1);
        PaymentOption paymentOption15 = new PaymentOption("우체국은행" , PaymentOptionType.DETAIL, "0015", 0, paymentOption1);
        PaymentOption paymentOption16 = new PaymentOption("우리은행" , PaymentOptionType.DETAIL, "0016", 0, paymentOption1);
        PaymentOption paymentOption17 = new PaymentOption("기업은행" , PaymentOptionType.DETAIL, "0017", 0, paymentOption1);
        PaymentOption paymentOption21 = new PaymentOption("현대카드" , PaymentOptionType.DETAIL, "0021", 1, paymentOption2);
        PaymentOption paymentOption22 = new PaymentOption("BC카드" , PaymentOptionType.DETAIL, "0022", 2, paymentOption2);
        PaymentOption paymentOption23 = new PaymentOption("KB국민카드" , PaymentOptionType.DETAIL, "0023", 1, paymentOption2);
        PaymentOption paymentOption24 = new PaymentOption("신한카드" , PaymentOptionType.DETAIL, "0024", 1, paymentOption2);
        PaymentOption paymentOption25 = new PaymentOption("삼성카드" , PaymentOptionType.DETAIL, "0025", 1, paymentOption2);
        PaymentOption paymentOption26 = new PaymentOption("롯데카드" , PaymentOptionType.DETAIL, "0026", 1, paymentOption2);
        PaymentOption paymentOption27 = new PaymentOption("우리카드" , PaymentOptionType.DETAIL, "0027", 1, paymentOption2);
        PaymentOption paymentOption28 = new PaymentOption("하나카드" , PaymentOptionType.DETAIL, "0028", 1, paymentOption2);
        PaymentOption paymentOption29 = new PaymentOption("농협카드" , PaymentOptionType.DETAIL, "0029", 1, paymentOption2);
        PaymentOption paymentOption31 = new PaymentOption("KAKAO Pay" , PaymentOptionType.DETAIL, "0031", 1, paymentOption3);
        PaymentOption paymentOption32 = new PaymentOption("Toss Pay" , PaymentOptionType.DETAIL, "0032", 2, paymentOption3);
        PaymentOption paymentOption33 = new PaymentOption("N Pay" , PaymentOptionType.DETAIL, "0033", 3, paymentOption3);
        /*
        PaymentOption paymentOption41 = new PaymentOption("SKT" , PaymentOptionType.DETAIL, "0041", 1, paymentOption4);
        PaymentOption paymentOption42 = new PaymentOption("KT" , PaymentOptionType.DETAIL, "0042", 2, paymentOption4);
        PaymentOption paymentOption43 = new PaymentOption("LG U+" , PaymentOptionType.DETAIL, "0043", 3, paymentOption4);
        PaymentOption paymentOption44 = new PaymentOption("알뜰폰" , PaymentOptionType.DETAIL, "0044", 4, paymentOption4);
         */
        paymentOptionRepository.saveAll(List.of(paymentOption1, paymentOption2, paymentOption3, paymentOption11, paymentOption12, paymentOption21, paymentOption22, paymentOption31, paymentOption32,
                paymentOption13, paymentOption14, paymentOption15, paymentOption16, paymentOption17,
                paymentOption23, paymentOption24, paymentOption25, paymentOption26, paymentOption27, paymentOption28, paymentOption29, paymentOption33));
        System.out.println("결제 옵션 데이터 27개  생성 완료");

    }
}