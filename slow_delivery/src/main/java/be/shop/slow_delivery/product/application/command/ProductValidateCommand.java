package be.shop.slow_delivery.product.application.command;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductValidateCommand {
    private final long productId;
    private final String productName;
    private final Money productPrice;
    private final Quantity orderQuantity;
    private final List<IngredientGroupValidateCommand> ingredientGroups;
    private final List<OptionGroupValidateCommand> optionGroups;

    public List<Long> getIngredientIds() {
        return ingredientGroups.stream()
                .flatMap(g -> g.getIngredients().stream())
                .map(IngredientValidateCommand::getIngredientId)
                .collect(Collectors.toList());
    }

    public List<Long> getOptionIds() {
        return optionGroups.stream()
                .flatMap(g -> g.getOptions().stream())
                .map(OptionValidateCommand::getOptionId)
                .collect(Collectors.toList());
    }
}
