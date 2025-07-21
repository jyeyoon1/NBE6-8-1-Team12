package com.caffe.domain.shipping.entity;

import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.shipping.constant.ShippingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Shipping {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private String email;         // 이메일
    private String address;       // 주소
    private int postcode;         // 우편번호
    private String contactName;   // 이름
    private String contactNumber; // 연락처
    private String carrier;       // 택배사

    @Enumerated(EnumType.STRING)
    private ShippingStatus status; // 배송 상태

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    // purchase 연결 필드 (현재 null 사용중)
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;


    public void assignInitialStatus() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();

        if (hour < 9) {
            this.status = ShippingStatus.BEFORE_DELIVERY;
        } else if (hour < 14) {
            this.status = ShippingStatus.DELIVERING;
        } else {
            this.status = ShippingStatus.BEFORE_DELIVERY;
        }
    }

    public void updateStatus(ShippingStatus newStatus, LocalDateTime modifyDate) {
        this.status = newStatus;
        this.modifyDate = modifyDate;
    }





}
