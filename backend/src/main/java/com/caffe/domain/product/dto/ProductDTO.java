package com.caffe.domain.product.dto;

import com.caffe.domain.product.entity.Product;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    @NotBlank(message = "상품명을 입력해주세요.")
    private String productName;
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private int price;
    @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
    private int totalQuantity;
    @Size(max = 1000, message = "설명은 최대 1000자까지 입력 가능합니다.")
    private String description;

    private String imageUrl;

    // DTO → Entity 변환 메서드
    public Product toEntity() {
        return new Product(
                this.productName,
                this.price,
                this.totalQuantity,
                this.description,
                this.imageUrl
        );
    }
}