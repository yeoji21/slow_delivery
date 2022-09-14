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
    public static class IngredientValidate {
        @Getter
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

    public void isSatisfy(IngredientGroupValidate opponent) {
        Assert.isTrue(id == opponent.id, "ingredient group id");
        Assert.isTrue(name.equals(opponent.name), "ingredient group name");
        selectCount.selectedCountCheck(opponent.selectCount.getMinCount());
        Assert.isTrue(isEqualList(ingredients, opponent.ingredients), "ingredients");
    }

    private <T> boolean isEqualList(final List<T> a, final List<T> b) {
        final Set<T> set = new HashSet<>(a);
        return a.size() == b.size() && set.containsAll(b);
    }
}
