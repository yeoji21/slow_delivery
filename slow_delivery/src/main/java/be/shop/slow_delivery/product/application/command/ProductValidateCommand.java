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
    private final List<IngredientValidateCommand> ingredients;
    private final List<OptionValidateCommand> options;


    public List<Long> getIngredientIds() {
        return ingredients
                .stream()
                .map(IngredientValidateCommand::getIngredientId)
                .collect(Collectors.toList());
    }

    public List<Long> getOptionIds() {
        return options
                .stream()
                .map(OptionValidateCommand::getOptionId)
                .collect(Collectors.toList());
    }
}
