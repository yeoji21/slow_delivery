package be.shop.slow_delivery.product.presentation.dto;

import be.shop.slow_delivery.common.domain.Quantity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductValidateDto {
    private long productId;
    private Quantity orderQuantity;
    private List<Long> ingredientIds;
    private List<Long> optionIds;

    @Builder
    public ProductValidateDto(long productId,
                              Quantity orderQuantity,
                              List<Long> ingredientIds,
                              List<Long> optionIds) {
        this.productId = productId;
        this.orderQuantity = orderQuantity;
        this.ingredientIds = ingredientIds;
        this.optionIds = optionIds;
    }
}
