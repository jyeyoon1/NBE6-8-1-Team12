package com.caffe.domain.payment.entity;

import com.caffe.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PaymentOption extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentOptionType type;

    // method는 null, detail은 method 부모 필수 - 체크 로직 필요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PaymentOption parent;

    @Column(unique = true, nullable = false)
    private String code;

    private int sortSeq;
}
