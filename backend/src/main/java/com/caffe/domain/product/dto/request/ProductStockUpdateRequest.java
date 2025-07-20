package com.caffe.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "상품 재고 수정 요청 DTO")
public record ProductStockUpdateRequest(
        @NotNull(message = "재고 수량을 입력해주세요.")
        @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
        @Schema(description = "변경할 재고 수량", example = "100")
        Integer totalQuantity
) {
}
