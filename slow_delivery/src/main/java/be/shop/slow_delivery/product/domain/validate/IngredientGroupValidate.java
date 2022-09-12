package be.shop.slow_delivery.product.domain.validate;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.product.domain.SelectCount;
import com.mysema.commons.lang.Assert;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class IngredientGroupValidate {
    private long id;
    private String name;
    private SelectCount selectCount;
    private List<IngredientValidate> ingredients;

    @EqualsAndHashCode
    @Getter
    public static class IngredientValidate {
        private long id;
        private String name;
        private Money price;

        @Builder @QueryProjection
        public IngredientValidate(long id,
                                  String name,
                                  Money price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    @Builder
    @QueryProjection
    public IngredientGroupValidate(long id,
                                   String name,
                                   SelectCount selectCount,
                                   List<IngredientValidate>ingredients) {
        this.id = id;
        this.name = name;
        this.selectCount = selectCount;
        this.ingredients = ingredients;
    }

    public void isEqualTo(IngredientGroupValidate validate) {
        Assert.isTrue(id == validate.id, "id");
        Assert.isTrue(name.equals(validate.name), "name");
        selectCount.selectedCountCheck(validate.selectCount.getMinCount());
        validate.getSelectCount().selectedCountCheck(selectCount.getMinCount());
        Assert.isTrue(isEqualTo(ingredients, validate.ingredients), "ingredients");
    }

    private <T> boolean isEqualTo(final List<T> a, final List<T> b) {
        final Set<T> set = new HashSet<>(a);
        return a.size() == b.size() && set.containsAll(b);
    }

}
