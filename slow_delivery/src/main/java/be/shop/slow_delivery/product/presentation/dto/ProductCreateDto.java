package be.shop.slow_delivery.product.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCreateDto {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private int price;
    @NotNull
    private int maxOrderQuantity;
    @NotNull
    private int stock;

    @Builder
    public ProductCreateDto(String name,
                            String description,
                            int price,
                            int maxOrderQuantity,
                            int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.maxOrderQuantity = maxOrderQuantity;
        this.stock = stock;
    }
}
