package com.caffe.domain.shipping.entity;

import com.caffe.domain.purchase.entity.Purchase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


/*
1. 택배(배송)를 만들고,
2. 내가 시킨 택배들을 보고,
3. 택배가 어디까지 갔는지(상태)를 보고,
4. 택배 상태를 바꿀 수 있게 만들어야 함..
*/


@Getter
@Setter
@Entity
public class Shipping {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Setter(PROTECTED)
    private int id; // 배송 번호

    private String address; // 주소
    private String contactNumber; // 연락처
    private String contactName; // 이름
    private String carrier; // 업체
    private char status; // 상태

    @CreatedDate
    private LocalDateTime createDate; // 등록 날짜

    @LastModifiedDate
    private LocalDateTime modifyDate; // 상태 업데이트 날짜

    @OneToOne
    private Purchase purchase; // 주문 번호
}
