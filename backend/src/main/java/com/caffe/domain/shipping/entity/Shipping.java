package com.caffe.domain.shipping.entity;

import com.caffe.domain.order.order.entity.Purchase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
public class Shipping {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Setter(PROTECTED)
    private int id; // 배송 번호

    private String address; // 주소
    private String contact_number; // 연락처
    private String contact_name; // 이름
    private String carrier; // 업체
    private char status; // 상태

    @CreatedDate
    private LocalDateTime createDate; // 등록 날짜

    @LastModifiedDate
    private LocalDateTime modifyDate; // 상태 업데이트 날짜

    @OneToOne
    private Purchase purchase; // 주문 번호
}
