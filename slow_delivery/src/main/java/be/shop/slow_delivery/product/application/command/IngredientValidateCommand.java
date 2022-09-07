package be.shop.slow_delivery.product.application.command;

import be.shop.slow_delivery.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IngredientValidateCommand {
    private long ingredientId;
    private String ingredientName;
    private Money ingredientPrice;

    @Builder
    public IngredientValidateCommand(long ingredientId,
                                     String ingredientName,
                                     int ingredientPrice) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.ingredientPrice = new Money(ingredientPrice);
    }
}
