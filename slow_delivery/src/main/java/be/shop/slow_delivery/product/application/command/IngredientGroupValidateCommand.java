package be.shop.slow_delivery.product.application.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class IngredientGroupValidateCommand {
    private long id;
    private String name;
    private List<IngredientValidateCommand> ingredients;

    @Builder
    public IngredientGroupValidateCommand(long id,
                                          String name,
                                          List<IngredientValidateCommand> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }
}
