package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.command.ProductValidateCommand;
import com.mysema.commons.lang.Assert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductValidationService {
    public Money validate(Product product,
                          Map<IngredientGroup, List<Ingredient>> ingredientsMap,
                          Map<OptionGroup, List<Option>> optionsMap,
                          ProductValidateCommand command) {
        return validateProduct(product, command.getOrderQuantity())
                .add(validateIngredients(ingredientsMap, command.getIngredientIds()))
                .add(validateOptions(optionsMap, command.getOptionIds()))
                .multiple(command.getOrderQuantity());
    }

    private Money validateProduct(Product product, Quantity orderQuantity) {
        Assert.isTrue(product.isOnSale(), "isSale");
        Assert.notNull(product.getMaxOrderQuantity().minus(orderQuantity), "orderQuantity");
        return product.getPrice();
    }

    private Money validateIngredients(Map<IngredientGroup, List<Ingredient>> ingredientsMap, List<Long> ingredientIds) {
        boolean[] checkIds = new boolean[ingredientIds.size()];
        Money totalAmount = Money.ZERO;
        for (Map.Entry<IngredientGroup, List<Ingredient>> entry : ingredientsMap.entrySet()) {
            List<Ingredient> ingredients = entry.getValue();
            totalAmount = totalAmount.add(entry.getKey().getIngredientsAmount(ingredients));
            ingredients.forEach(ingredient -> checkIds[ingredientIds.indexOf(ingredient.getId())] = true);
        }
        checkingIds(ingredientIds, checkIds, "ingredient");
        return totalAmount;
    }

    private Money validateOptions(Map<OptionGroup, List<Option>> optionsMap, List<Long> optionIds) {
        boolean[] checkIds = new boolean[optionIds.size()];
        Money totalAmount = Money.ZERO;

        for (Map.Entry<OptionGroup, List<Option>> entry : optionsMap.entrySet()) {
            List<Option> options = entry.getValue();
            totalAmount = totalAmount.add(entry.getKey().getOptionsAmount(options));
            options.forEach(option -> checkIds[optionIds.indexOf(option.getId())] = true);
        }
        checkingIds(optionIds, checkIds, "option");
        return totalAmount;
    }

    private void checkingIds(List<Long> ingredientIds, boolean[] checkIds, String type) {
        for (int i = 0; i < checkIds.length; i++) {
            if(!checkIds[i]) {
                throw new IllegalArgumentException(type + " id " + ingredientIds.get(i));
            }
        }
    }
}
