package com.caffe.domain.purchase.entity;

import com.caffe.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Purchase extends BaseEntity {
    private String userEmail;

    private double totalPrice;

    @Setter // 결제 상태에 따라 변경 필요
    @Enumerated(EnumType.STRING)
    private PurchaseStatus status = PurchaseStatus.TEMPORARY;

    @OneToMany(mappedBy = "purchase", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

    public Purchase(String userEmail) {
        this.userEmail = userEmail;
        this.totalPrice = 0;
    }

    public void addPurchaseItem(PurchaseItem purchaseItem) {
        purchaseItems.add(purchaseItem);
        purchaseItem.setPurchase(this);
        calcTotalPrice();
    }

    public void calcTotalPrice() {
        totalPrice = purchaseItems
                .stream()
                .mapToDouble(PurchaseItem::getItemTotalPrice)
                .sum();
    }
}
