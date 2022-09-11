package be.shop.slow_delivery.product.domain.validate;

import be.shop.slow_delivery.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IngredientValidate {
    private long id;
    private String name;
    private Money price;

    @Builder
    public IngredientValidate(long id,
                              String name,
                              Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
