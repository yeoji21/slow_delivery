package be.shop.slow_delivery.product.application.command;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductValidateCommand {
    private final long id;
    private final String name;
    private final Money price;
    private final Quantity orderQuantity;
    private final List<IngredientGroupValidateCommand> ingredientGroups;
    private final List<OptionGroupValidateCommand> optionGroups;

    public Map<Long, List<Long>> getOptionIdMap() {
        return getOptionGroups()
                .stream()
                .collect(groupingBy(OptionGroupValidateCommand::getId,
                                flatMapping(group -> group
                                        .getOptions()
                                        .stream()
                                        .map(OptionValidateCommand::getId), toList())
                        )
                );
    }

    public Map<Long, List<Long>> getIngredientIdMap() {
        return getIngredientGroups()
                .stream()
                .collect(groupingBy(IngredientGroupValidateCommand::getId,
                                flatMapping(group -> group
                                        .getIngredients()
                                        .stream()
                                        .map(IngredientValidateCommand::getId), toList())
                        )
                );
    }
}
