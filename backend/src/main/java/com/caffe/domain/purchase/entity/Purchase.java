package com.caffe.domain.purchase.entity;

import com.caffe.domain.purchase.constant.PurchaseStatus;
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

    private int totalPrice;

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

    public void completePurchase() {
        if (this.status != PurchaseStatus.TEMPORARY) {
            throw new IllegalStateException("임시 상태가 아닌 주문은 완료할 수 없습니다.");
        }
        this.status = PurchaseStatus.PURCHASED;
    }

    public void cancelOrder() {
        if (!(this.status == PurchaseStatus.TEMPORARY || this.status == PurchaseStatus.PURCHASED)) {
            throw new IllegalStateException("임시 상태 또는 완료된 주문만 취소할 수 있습니다.");
        }
        this.status = PurchaseStatus.CANCELED;
    }

    public void failPayment() {
        this.status = PurchaseStatus.FAILED;
    }

    public void calcTotalPrice() {
        totalPrice = purchaseItems
                .stream()
                .mapToInt(PurchaseItem::getItemTotalPrice)
                .sum();
    }

    public int getTotalQuantity() {
        return purchaseItems
                .stream()
                .mapToInt(PurchaseItem::getQuantity)
                .sum();
    }

    public String representativeProductName() {
        return purchaseItems
                .getFirst()
                .getProduct()
                .getProductName();
    }

    public String summaryName() {
        int size = purchaseItems.size();
        if (size <= 1) {
            return representativeProductName();
        }
        return "%s 외 %s건".formatted(representativeProductName(), purchaseItems.size());
    }
}
