package com.caffe.domain.payment.entity;


import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Payment extends BaseEntity {

    private String method;
    private char status;
    @OneToOne
    private Purchase purchase;
}
