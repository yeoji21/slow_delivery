package be.shop.slow_delivery.product.domain.validate;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import com.mysema.commons.lang.Assert;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductValidate {
    private long id;
    private String name;
    private Money price;
    private Quantity orderQuantity;

    @Builder @QueryProjection
    public ProductValidate(long id,
                           String name,
                           Money price,
                           Quantity orderQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.orderQuantity = orderQuantity;
    }

    public void isEqualTo(ProductValidate validate) {
        Assert.isTrue(id == validate.getId(), "id");
        Assert.isTrue(name.equals(validate.getName()), "name");
        Assert.isTrue(price.equals(validate.price), "price");
        Assert.isTrue(orderQuantity.minus(validate.getOrderQuantity()).toInt() >= 0, "orderQuantity");
    }
}
