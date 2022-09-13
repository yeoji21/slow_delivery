package be.shop.slow_delivery.product.application.criteria;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class IngredientGroupValidateCriteria {
    private long id;
    private String name;
    private List<IngredientValidateCriteria> ingredients;

    @Builder
    public IngredientGroupValidateCriteria(long id,
                                           String name,
                                           List<IngredientValidateCriteria> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }
}
