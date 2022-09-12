package be.shop.slow_delivery.product.application.command;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.domain.SelectCount;
import be.shop.slow_delivery.product.domain.validate.IngredientGroupValidate;
import be.shop.slow_delivery.product.domain.validate.OptionGroupValidate;
import be.shop.slow_delivery.product.domain.validate.ProductValidate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public ProductValidate toProductValidate() {
        return ProductValidate.builder()
                .id(id)
                .name(name)
                .price(price)
                .orderQuantity(orderQuantity)
                .build();
    }


    // TODO: 2022/09/12 mapper로 이동
    public List<IngredientGroupValidate> toIngredientGroupValidate() {
        return ingredientGroups
                .stream()
                .map(g -> {
                    List<IngredientGroupValidate.IngredientValidate> ingredients = g.getIngredients()
                            .stream()
                            .map(i -> IngredientGroupValidate.IngredientValidate.builder()
                                    .id(i.getId())
                                    .name(i.getName())
                                    .price(i.getPrice())
                                    .build()
                            )
                            .collect(Collectors.toList());

                    return IngredientGroupValidate.builder()
                            .id(g.getId())
                            .name(g.getName())
                            .selectCount(new SelectCount(ingredients.size(), 100))
                            .ingredients(ingredients)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<OptionGroupValidate> toOptionGroupValidate() {
        return optionGroups
                .stream()
                .map(g -> {
                    List<OptionGroupValidate.OptionValidate> options = g.getOptions()
                            .stream()
                            .map(i -> OptionGroupValidate.OptionValidate.builder()
                                    .id(i.getId())
                                    .name(i.getName())
                                    .price(i.getPrice())
                                    .build()
                            )
                            .collect(toList());

                    return OptionGroupValidate.builder()
                            .id(g.getId())
                            .name(g.getName())
                            .maxSelectCount(new Quantity(options.size()))
                            .options(options)
                            .build();
                })
                .collect(Collectors.toList());
    }

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
