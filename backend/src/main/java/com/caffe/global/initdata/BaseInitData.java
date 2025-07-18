package com.caffe.global.initdata;

import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.entity.Role;
import com.caffe.domain.member.repository.MemberRepository;
import com.caffe.domain.payment.entity.Payment;
import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.constant.PaymentOptionType;
import com.caffe.domain.payment.repository.PaymentOptionRepository;
import com.caffe.domain.payment.repository.PaymentRepository;
import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.repository.ProductRepository;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.entity.PurchaseItem;
import com.caffe.domain.purchase.constant.PurchaseStatus;
import com.caffe.domain.purchase.repository.PurchaseRepository;
import com.caffe.domain.shipping.constant.ShippingStatus;
import com.caffe.domain.shipping.entity.Shipping;
import com.caffe.domain.shipping.repository.ShippingRepository;
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
    private final PurchaseRepository purchaseRepository;
    private final PaymentRepository paymentRepository;
    private final ShippingRepository shippingRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationContext applicationContext;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            BaseInitData self = applicationContext.getBean(BaseInitData.class);
            self.newAdmin();
            self.initProducts();
            self.initPaymentOptions();
            self.initPurchase();
            self.initPayment();
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
                "https://i.imgur.com/HKOFQYa.jpeg"
        );

        Product product2 = new Product(
                "Ethiopian Yirgacheffe",
                6500,
                50,
                "플로럴 향이 풍부한 에티오피아 예가체프 커피입니다.",
                "https://m.media-amazon.com/images/I/71LN-graGzL._UF894%2C1000_QL80_.jpg"
        );

        productRepository.saveAll(List.of(product1, product2));
        System.out.println("기본 상품 2개 생성 완료");
    }

    @Transactional
    public void initPaymentOptions() {
        if (paymentOptionRepository.count() > 0) return;
        PaymentOption paymentOption1 = new PaymentOption("BANK", PaymentOptionType.TOP_LEVEL, "0001", 1);
        PaymentOption paymentOption2 = new PaymentOption("CARD", PaymentOptionType.TOP_LEVEL, "0002", 2);
        PaymentOption paymentOption3 = new PaymentOption("PG", PaymentOptionType.TOP_LEVEL, "0003", 3);
        PaymentOption paymentOption11 = new PaymentOption("NH" , PaymentOptionType.DETAIL, "0004", 1, paymentOption1);
        PaymentOption paymentOption12 = new PaymentOption("HN" , PaymentOptionType.DETAIL, "0005", 2, paymentOption1);
        PaymentOption paymentOption21 = new PaymentOption("HD" , PaymentOptionType.DETAIL, "0006", 1, paymentOption2);
        PaymentOption paymentOption22 = new PaymentOption("BC" , PaymentOptionType.DETAIL, "0007", 2, paymentOption2);
        PaymentOption paymentOption31 = new PaymentOption("KAKAO" , PaymentOptionType.DETAIL, "0008", 1, paymentOption3);
        PaymentOption paymentOption32 = new PaymentOption("Toss" , PaymentOptionType.DETAIL, "0009", 2, paymentOption3);

        paymentOptionRepository.saveAll(List.of(paymentOption1, paymentOption2, paymentOption3, paymentOption11, paymentOption12, paymentOption21, paymentOption22, paymentOption31, paymentOption32));
        System.out.println("결제 옵션 데이터 9개  생성 완료");

    }

    // 상세페이지에서 주문 (주문:구매제품 1:1)
    @Transactional
    public void initPurchase() {
        if (purchaseRepository.count() > 0) return;

        Product product = productRepository.findById(1).get();
        Product product2 = productRepository.findById(2).get();

        Purchase purchase1 = new Purchase("test1@email.com");
        purchase1.setStatus(PurchaseStatus.ORDERED);
        PurchaseItem purchaseItem1 = new PurchaseItem(2, product);
        purchase1.addPurchaseItem(purchaseItem1);
        purchaseRepository.save(purchase1);

        Purchase purchase2 = new Purchase("test2@email.com");
        purchase2.setStatus(PurchaseStatus.ORDERED);
        PurchaseItem purchaseItem2 = new PurchaseItem(5, product);
        purchase2.addPurchaseItem(purchaseItem2);
        purchaseRepository.save(purchase2);

        Purchase purchase3 = new Purchase("test3@email.com");
        purchase3.setStatus(PurchaseStatus.ORDERED);
        PurchaseItem purchaseItem3 = new PurchaseItem(12, product2);
        purchase3.addPurchaseItem(purchaseItem3);
        purchaseRepository.save(purchase3);

        Purchase purchase4 = new Purchase("test4@email.com");
        purchase4.setStatus(PurchaseStatus.TEMPORARY);
        PurchaseItem purchaseItem4 = new PurchaseItem(33, product);
        purchase4.addPurchaseItem(purchaseItem4);
        purchaseRepository.save(purchase4);

        Purchase purchase5 = new Purchase("test5@email.com");
        purchase5.setStatus(PurchaseStatus.TEMPORARY);
        PurchaseItem purchaseItem5 = new PurchaseItem(1, product2);
        purchase5.addPurchaseItem(purchaseItem5);
        purchaseRepository.save(purchase5);

        // 배송 정보
        Shipping shipping = Shipping.builder()
                .address("경기도 시흥시")
                .postcode(12345)
                .contactNumber("010-1234-4567")
                .contactName("이아무개")
                .carrier("CJ대한통운")
                .status(ShippingStatus.DELIVERED)
                .purchase(purchase1)
                .build();
        shippingRepository.save(shipping);

        Shipping shipping2 = Shipping.builder()
                .address("경기도 시흥시")
                .postcode(65423)
                .contactNumber("010-5432-4567")
                .contactName("홍길동")
                .carrier("CJ대한통운")
                .status(ShippingStatus.DELIVERING)
                .purchase(purchase2)
                .build();
        shippingRepository.save(shipping2);

        Shipping shipping3 = Shipping.builder()
                .address("서울특별시 도봉구")
                .postcode(23456)
                .contactNumber("010-9876-1234")
                .contactName("짱구")
                .carrier("CJ대한통운")
                .status(ShippingStatus.BEFORE_DELIVERY)
                .purchase(purchase3)
                .build();
        shippingRepository.save(shipping3);

        Shipping shipping4 = Shipping.builder()
                .address("서울특별시 도봉구")
                .postcode(76432)
                .contactNumber("010-7643-2345")
                .contactName("고길동")
                .carrier("CJ대한통운")
                .status(ShippingStatus.TEMPORARY)
                .purchase(purchase4)
                .build();
        shippingRepository.save(shipping4);

        Shipping shipping5 = Shipping.builder()
                .address("전라남도 해남군")
                .postcode(87543)
                .contactNumber("010-9993-6643")
                .contactName("대흥사")
                .carrier("CJ대한통운")
                .status(ShippingStatus.TEMPORARY)
                .purchase(purchase5)
                .build();
        shippingRepository.save(shipping5);
    }

    @Transactional
    public void initPayment() {
        if (paymentRepository.count() > 0) return;
        Purchase purchase1 = purchaseRepository.findById(1).get();
        PaymentOption paymentOption1 = paymentOptionRepository.findById(4).get();

        Payment payment1 = new Payment(purchase1.getTotalPrice(), purchase1, paymentOption1);
        paymentRepository.save(payment1);

        Purchase purchase2 = purchaseRepository.findById(2).get();
        PaymentOption paymentOption2 = paymentOptionRepository.findById(7).get();

        Payment payment2 = new Payment(purchase2.getTotalPrice(), purchase2, paymentOption2);
        paymentRepository.save(payment2);

        System.out.println("결제  데이터 2개  생성 완료");
    }
}