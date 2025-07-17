package com.caffe.domain.shipping.entity;

import com.caffe.domain.purchase.entity.Purchase;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shipping {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id; // 배송 번호

    private String address; // 주소
    private String contactNumber; // 연락처
    private String contactName; // 이름
    private String carrier; // 업체
    private int postcode; // 우편번호


    @Enumerated(EnumType.STRING)
    private ShippingStatus status;

    @CreatedDate
    private LocalDateTime createDate; // 등록 날짜

    @LastModifiedDate
    private LocalDateTime modifyDate; // 상태 업데이트 날짜

    /*
     - Purchase와 다대일(N:1) 관계
     - 하나의 Purchase에 여러 배송이 연결될 수 있음
     - @JsonBackReference: 직렬화 시 순환참조 방지 (JSON 변환 시 Purchase는 직렬화 X)
     */
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    @JsonBackReference
    private Purchase purchase; // 주문 번호


    public Shipping(String address, int postcode, String contactNumber, String contactName,
                    String carrier, ShippingStatus status, Purchase purchase) {
        this.address = address;
        this.postcode = postcode;
        this.contactNumber = contactNumber;
        this.contactName = contactName;
        this.carrier = carrier;
        this.status = status;
        this.purchase = purchase;
        this.createDate = LocalDateTime.now();
    }
}