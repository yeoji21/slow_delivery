package be.shop.slow_delivery.product.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IngredientGroupValidateDto {
    private long id;
    private String name;
    private List<IngredientValidateDto> ingredients;

    @Builder
    public IngredientGroupValidateDto(long id,
                                      String name,
                                      List<IngredientValidateDto> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }
}
