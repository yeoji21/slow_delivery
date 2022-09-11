package be.shop.slow_delivery.product.domain.validate;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductValidate {
    private long id;
    private String name;
    private Money price;
    private Quantity orderQuantity;

    @Builder
    public ProductValidate(long id,
                           String name,
                           Money price,
                           Quantity orderQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.orderQuantity = orderQuantity;
    }
}
