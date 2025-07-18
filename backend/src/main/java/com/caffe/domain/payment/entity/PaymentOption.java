package com.caffe.domain.payment.entity;

import com.caffe.global.constant.PaymentOptionType;
import com.caffe.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
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

    public PaymentOption(String name, PaymentOptionType type, String code, int sortSeq) {
        this.name = name;
        this.type = type;
        this.code = code;
        this.sortSeq = sortSeq;
    }

    public PaymentOption(String name, PaymentOptionType type, String code, int sortSeq, PaymentOption parent) {
        this.name = name;
        this.type = type;
        this.code = code;
        this.sortSeq = sortSeq;
        this.parent = parent;
    }
}
