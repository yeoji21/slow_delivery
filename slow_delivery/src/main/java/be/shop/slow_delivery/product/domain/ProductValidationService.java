package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.product.application.command.ProductValidateCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductValidationService {
    public void validate(Product product,
                          Map<IngredientGroup, List<Ingredient>> ingredientsMap,
                          Map<OptionGroup, List<Option>> optionsMap,
                          ProductValidateCommand command) {


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
