package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.Quantity;
import com.mysema.commons.lang.Assert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductValidationService {
    public void validateProduct(Product product, Quantity orderQuantity) {
        Assert.isTrue(product.isOnSale(), "isSale");
        Assert.notNull(product.getMaxOrderQuantity().minus(orderQuantity), "orderQuantity");
    }

    // TODO: 2022/08/27 쿼리에서 판매중인지 확인하는 조건
    public void validateIngredients(Map<IngredientGroup, List<Ingredient>> ingredientsMap, List<Long> ingredientIds) {
        boolean[] checkIds = new boolean[ingredientIds.size()];
        for (Map.Entry<IngredientGroup, List<Ingredient>> entry : ingredientsMap.entrySet()) {
            List<Ingredient> ingredients = entry.getValue();
            entry.getKey().validate(ingredients);
            ingredients.forEach(ingredient -> checkIds[ingredientIds.indexOf(ingredient.getId())] = true);
        }

        for (int i = 0; i < checkIds.length; i++) {
            if(!checkIds[i])
                throw new IllegalArgumentException("ingredient id " + ingredientIds.get(i));
        }
    }

    public void validateOptions(Map<OptionGroup, List<Option>> optionsMap, List<Long> optionIds) {
        boolean[] checkIds = new boolean[optionIds.size()];
        for (Map.Entry<OptionGroup, List<Option>> entry : optionsMap.entrySet()) {
            List<Option> options = entry.getValue();
            entry.getKey().validate(options);
            options.forEach(option -> checkIds[optionIds.indexOf(option.getId())] = true);
        }

        for (int i = 0; i < checkIds.length; i++) {
            if(!checkIds[i])
                throw new IllegalArgumentException("option id " + optionIds.get(i));
        }
    }
}
