package com.caffe.domain.payment.entity;


import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Payment extends BaseEntity {
    private String paymentInfo;
    private int amount;
    private char status;
    @OneToOne
    private Purchase purchase;

    @ManyToOne
    private PaymentOption paymentOption;

    public Payment(String paymentInfo, int amount, Purchase purchase,  PaymentOption paymentOption) {
        this.paymentInfo = paymentInfo;
        this.amount = amount;
        this.status = 'R';
        this.purchase = purchase;
        this.paymentOption = paymentOption;
    }

    public void updateStatus(char status) {
        this.status = status;
    }

    public void updatePayment(PaymentOption paymentOption, String paymentInfo, int amount) {
        this.paymentOption = paymentOption;
        this.paymentInfo = paymentInfo;
        this.amount = amount;
        this.status = 'R';
    }
}
