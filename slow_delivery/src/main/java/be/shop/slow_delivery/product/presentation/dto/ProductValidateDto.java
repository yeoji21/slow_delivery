package be.shop.slow_delivery.product.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductValidateDto {
    private long id;
    private String name;
    private int price;
    private int orderQuantity;
    private List<IngredientGroupValidateDto> ingredientGroups;
    private List<OptionGroupValidateDto> optionGroups;

    @Builder
    public ProductValidateDto(long id,
                              String name,
                              int price,
                              int orderQuantity,
                              List<IngredientGroupValidateDto> ingredientGroups,
                              List<OptionGroupValidateDto> optionGroups) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.orderQuantity = orderQuantity;
        this.ingredientGroups = ingredientGroups;
        this.optionGroups = optionGroups;
    }
}
