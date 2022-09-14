package be.shop.slow_delivery.product.domain.validate;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import com.mysema.commons.lang.Assert;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.function.Predicate;

@Getter
public class ProductValidate {
    private long id;
    private String name;
    private Money price;
    private Quantity orderQuantity;
    private List<IngredientGroupValidate> ingredientGroupValidates;
    private List<OptionGroupValidate> optionGroupValidates;

    @Builder
    public ProductValidate(long id,
                           String name,
                           Money price,
                           Quantity orderQuantity,
                           List<IngredientGroupValidate> ingredientGroupValidates,
                           List<OptionGroupValidate> optionGroupValidates) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.orderQuantity = orderQuantity;
        this.ingredientGroupValidates = ingredientGroupValidates;
        this.optionGroupValidates = optionGroupValidates;
    }

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

    public void setIngredientGroupValidates(List<IngredientGroupValidate> ingredientGroupValidates) {
        this.ingredientGroupValidates = ingredientGroupValidates;
    }

    public void setOptionGroupValidates(List<OptionGroupValidate> optionGroupValidates) {
        this.optionGroupValidates = optionGroupValidates;
    }

    public void isSatisfy(ProductValidate validate) {
        Assert.isTrue(id == validate.getId(), "product id");
        Assert.isTrue(name.equals(validate.getName()), "name");
        Assert.isTrue(price.equals(validate.price), "price");
        Assert.isTrue(orderQuantity.toInt() >= validate.getOrderQuantity().toInt(), "orderQuantity");

        for (IngredientGroupValidate group : ingredientGroupValidates) {
            group.isSatisfy(findOne(validate.getIngredientGroupValidates(), g -> g.getId() == group.getId()));
        }

        for (OptionGroupValidate group : optionGroupValidates) {
            group.isSatisfy(findOne(validate.getOptionGroupValidates(), g -> g.getId() == group.getId()));
        }
    }

    private <T> T findOne(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .findAny()
                .orElseThrow();
    }
}
