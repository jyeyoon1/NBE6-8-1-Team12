package com.caffe.domain.payment.entity;


import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Payment extends BaseEntity {
    private String paymentInfo;
    private int amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @OneToOne
    private Purchase purchase;

    @ManyToOne
    private PaymentOption paymentOption;

    public Payment(String paymentInfo, int amount, Purchase purchase,  PaymentOption paymentOption) {
        this.paymentInfo = paymentInfo;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.purchase = purchase;
        this.paymentOption = paymentOption;
    }

    public void isSuccess(boolean result) {
        this.status = result ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELED;
    }

    public void updatePayment(PaymentOption paymentOption, String paymentInfo, int amount) {
        this.paymentOption = paymentOption;
        this.paymentInfo = paymentInfo;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }
}
