package com.caffe.domain.purchase.entity;

import com.caffe.domain.product.entity.Product;
import com.caffe.global.jpa.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PurchaseItem extends BaseEntity {
    private int quantity;
    private double price;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JsonIgnore
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Purchase purchase;
}
