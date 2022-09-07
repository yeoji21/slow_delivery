package be.shop.slow_delivery.product.application.command;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ProductCreateCommand {
    private String name;
    private String description;
    private Money price;
    private Quantity maxOrderQuantity;
    private Quantity stock;
}
