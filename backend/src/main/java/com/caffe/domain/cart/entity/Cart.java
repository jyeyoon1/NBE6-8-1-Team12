package com.caffe.domain.cart.entity;

import com.caffe.domain.product.entity.Product;
import com.caffe.global.jpa.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {
    private int quantity;
    private int price;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JsonIgnore
    private Product product;

    public Cart(Product product, int quantity) {
        this.quantity = quantity;
        this.price = product.getPrice();
        this.product = product;
    }

    public int getItemTotalPrice() {
        return quantity * price;
    }
}
