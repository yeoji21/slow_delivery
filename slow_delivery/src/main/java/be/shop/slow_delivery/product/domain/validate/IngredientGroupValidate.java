package be.shop.slow_delivery.product.domain.validate;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class IngredientGroupValidate {
    private long id;
    private String name;
    private List<IngredientValidate> ingredients;

    @Builder
    public IngredientGroupValidate(long id,
                                   String name,
                                   List<IngredientValidate> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }
}
