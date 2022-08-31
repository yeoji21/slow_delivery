package be.shop.slow_delivery.product.presentation.dto;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
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
    private Money price;
    @NotNull
    private Quantity maxOrderQuantity;
    @NotNull
    private Quantity stock;

    @Builder
    public ProductCreateDto(String name,
                            String description,
                            Money price,
                            Quantity maxOrderQuantity,
                            Quantity stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.maxOrderQuantity = maxOrderQuantity;
        this.stock = stock;
    }
}
