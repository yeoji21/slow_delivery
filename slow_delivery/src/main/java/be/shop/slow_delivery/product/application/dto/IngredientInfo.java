package be.shop.slow_delivery.product.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IngredientInfo {
    private long ingredientId;
    private String name;
    private int price;

    @Builder @QueryProjection
    public IngredientInfo(long ingredientId,
                          String name,
                          int price) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.price = price;
    }
}
