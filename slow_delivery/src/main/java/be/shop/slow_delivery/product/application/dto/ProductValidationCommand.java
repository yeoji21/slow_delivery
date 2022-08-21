package be.shop.slow_delivery.product.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductValidationCommand {
    private final long productId;
    private final List<Long> ingredientIds;
    private final List<Long> optionIds;

    @Builder
    public ProductValidationCommand(long productId, List<Long> ingredientIds, List<Long> optionIds) {
        this.productId = productId;
        this.ingredientIds = ingredientIds;
        this.optionIds = optionIds;
    }
}
