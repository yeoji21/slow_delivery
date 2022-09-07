package be.shop.slow_delivery.product.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductValidateDto {
    private long productId;
    private String productName;
    private int productPrice;
    private int orderQuantity;
    private List<IngredientValidateDto> ingredients;
    private List<OptionValidateDto> options;

    @Builder
    public ProductValidateDto(long productId,
                              String productName,
                              int productPrice,
                              int orderQuantity,
                              List<IngredientValidateDto> ingredients,
                              List<OptionValidateDto> options) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.orderQuantity = orderQuantity;
        this.ingredients = ingredients;
        this.options = options;
    }
}
