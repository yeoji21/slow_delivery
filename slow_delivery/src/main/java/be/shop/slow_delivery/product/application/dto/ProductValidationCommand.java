package be.shop.slow_delivery.product.application.dto;

import be.shop.slow_delivery.common.domain.Quantity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductValidationCommand {
    private final long productId;
    private final Quantity orderQuantity;
    private final List<Long> ingredientIds;
    private final List<Long> optionIds;
}
