package com.caffe.domain.product.dto.request;

import com.caffe.domain.product.entity.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "상품 상태 변경 요청 DTO")
public record ProductStatusUpdateRequest(
        @NotNull(message = "내용을 입력해주세요.")
        @Schema(description = "변경할 상품 상태", example = "ON_SALE")
        ProductStatus status
) {
}
