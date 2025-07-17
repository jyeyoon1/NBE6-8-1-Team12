package com.caffe.domain.purchase.entity;

import com.caffe.domain.product.entity.Product;
import com.caffe.domain.purchase.dto.req.PurchaseReqDto;
import com.caffe.global.jpa.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseItem extends BaseEntity {
    private int quantity;
    private int price;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JsonIgnore
    private Product product;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Purchase purchase;

    public PurchaseItem(int quantity, Product product) {
        this.quantity = quantity;
        this.price = product.getPrice();
        this.product = product;
    }

    public PurchaseItem(PurchaseReqDto dto, Product product) {
        this.quantity = dto.quantity();
        this.price = product.getPrice();
        this.product = product;
    }

    public int getItemTotalPrice() {
        return quantity * price;
    }
}
