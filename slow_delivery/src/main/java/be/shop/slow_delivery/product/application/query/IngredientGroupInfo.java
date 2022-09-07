package be.shop.slow_delivery.product.application.query;

import be.shop.slow_delivery.common.domain.Quantity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class IngredientGroupInfo {
    private long ingredientGroupId;
    private String name;
    private int minSelectCount;
    private int maxSelectCount;
    private List<IngredientInfo> ingredients;

    @Builder @QueryProjection
    public IngredientGroupInfo(long ingredientGroupId,
                               String name,
                               Quantity minSelectCount,
                               Quantity maxSelectCount,
                               List<IngredientInfo> ingredients) {
        this.ingredientGroupId = ingredientGroupId;
        this.name = name;
        this.minSelectCount = minSelectCount.toInt();
        this.maxSelectCount = maxSelectCount.toInt();
        this.ingredients = ingredients;
    }
}
