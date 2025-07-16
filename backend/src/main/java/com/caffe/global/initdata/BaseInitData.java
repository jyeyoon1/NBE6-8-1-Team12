package com.caffe.global.initdata;

import com.caffe.domain.member.entity.Member;
import com.caffe.domain.member.entity.Role;
import com.caffe.domain.member.repository.MemberRepository;
import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.entity.PaymentOptionType;
import com.caffe.domain.payment.repository.PaymentOptionRepository;
import com.caffe.domain.product.entity.Product;
import com.caffe.domain.product.repository.ProductRepository;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.entity.PurchaseItem;
import com.caffe.domain.purchase.entity.PurchaseStatus;
import com.caffe.domain.purchase.repository.PurchaseRepository;
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
        };
    }

    @Transactional
    public void newAdmin() {
        if (memberRepository.count() > 0) return;
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setPassword(passwordEncoder.encode("test"));
        member.setRole(Role.ADMIN);
        memberRepository.save(member);
        System.out.println("초기 멤버 생성 완료");
    }

    @Transactional
    public void initProducts() {
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

    @Transactional
    public void initPaymentOptions() {
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

    // 상세페이지에서 주문 (주문:구매제품 1:1)
    // setter -> 생성자로 변경 필요해 보임
    @Transactional
    public void initPurchase() {
        if (purchaseRepository.count() > 0) return;

        Product product = productRepository.findById(1).get();
        Product product2 = productRepository.findById(2).get();

        // Purchase1 저장
        int quantity1 = 2;
        Purchase purchase1 = new Purchase();
        purchase1.setUserEmail("test1@email.com");
        purchase1.setTotalPrice(product.getPrice() * quantity1);
        purchase1.setStatus(PurchaseStatus.ORDERED);

        // PurchaseItem1 저장
        PurchaseItem purchaseItem1 = new PurchaseItem();
        purchaseItem1.setPrice(product.getPrice());
        purchaseItem1.setQuantity(quantity1);
        purchaseItem1.setProduct(product);

        purchase1.addPurchaseItem(purchaseItem1);
        purchaseRepository.save(purchase1);


        // Purchase2 저장
        int quantity2 = 5;
        Purchase purchase2 = new Purchase();
        purchase2.setUserEmail("test2@email.com");
        purchase2.setTotalPrice(product.getPrice() * quantity2);
        purchase2.setStatus(PurchaseStatus.ORDERED);

        // PurchaseItem2 저장
        PurchaseItem purchaseItem2 = new PurchaseItem();
        purchaseItem2.setPrice(product.getPrice());
        purchaseItem2.setQuantity(quantity2);
        purchaseItem2.setProduct(product);

        purchase2.addPurchaseItem(purchaseItem2);
        purchaseRepository.save(purchase2);


        // Purchase3 저장
        int quantity3 = 12;
        Purchase purchase3 = new Purchase();
        purchase3.setUserEmail("test3@email.com");
        purchase3.setTotalPrice(product2.getPrice() * quantity3);
        purchase3.setStatus(PurchaseStatus.ORDERED);

        // PurchaseItem3 저장
        PurchaseItem purchaseItem3 = new PurchaseItem();
        purchaseItem3.setPrice(product2.getPrice());
        purchaseItem3.setQuantity(quantity3);
        purchaseItem3.setProduct(product2);

        purchase3.addPurchaseItem(purchaseItem3);
        purchaseRepository.save(purchase3);

        // 배송 정보 데이터 필요
    }
}