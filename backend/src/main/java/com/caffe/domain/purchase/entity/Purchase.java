package com.caffe.domain.purchase.entity;

import com.caffe.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

@Getter
@Setter
@Entity
public class Purchase extends BaseEntity {
    private String userEmail;
    private char status;

    @OneToMany(mappedBy = "purchase", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

    public void addPurchaseItem(PurchaseItem purchaseItem) {
        purchaseItems.add(purchaseItem);
        purchaseItem.setPurchase(this);
    }
}
