package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.exception.ErrorCode;
import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.product.application.command.ProductCommandMapper;
import be.shop.slow_delivery.product.application.command.ProductCreateCommand;
import be.shop.slow_delivery.product.application.command.ProductValidateCommand;
import be.shop.slow_delivery.product.domain.*;
import be.shop.slow_delivery.product.domain.validate.IngredientGroupValidate;
import be.shop.slow_delivery.product.domain.validate.IngredientValidate;
import be.shop.slow_delivery.product.domain.validate.ProductValidate;
import be.shop.slow_delivery.stock.application.StockCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final ProductValidationService validationService;
    private final StockCommandService stockCommandService;
    private final ProductCommandMapper productCommandMapper;

    @Transactional(readOnly = true)
    public void validateOrder(ProductValidateCommand command) {
        Product product = productRepository.findById(command.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        product.validate(getProductValidate(command));

        // 조회도 엔티티가 아닌 IngredientGroupValidate로?
        Map<IngredientGroup, List<Ingredient>> ingredientMap = productRepository
                .findIngredientMap(command.getId(), command.getIngredientIdMap());
        ingredientMapValidate(command, ingredientMap);

        Map<OptionGroup, List<Option>> optionMap = productRepository
                .findOptionMap(product.getId(), command.getOptionIdMap());


    }

    private void ingredientMapValidate(ProductValidateCommand command, Map<IngredientGroup, List<Ingredient>> ingredientsMap) {
        for (IngredientGroup group : ingredientsMap.keySet()) {
            IngredientGroupValidate validate = find(getIngredientGroupValidates(command), g -> g.getId() == group.getId());
            group.validate(validate);
            ingredientsMap.get(group)
                    .forEach(ingredient -> ingredient.validate(
                                    find(validate.getIngredients(), i -> i.getId() == ingredient.getId())
                            )
                    );
        }
    }

    private List<IngredientGroupValidate> getIngredientGroupValidates(ProductValidateCommand command) {
        return command.getIngredientGroups()
                .stream()
                .map(g -> {
                    List<IngredientValidate> ingredients = g.getIngredients()
                            .stream()
                            .map(i -> IngredientValidate.builder()
                                    .id(i.getId())
                                    .name(i.getName())
                                    .price(i.getPrice())
                                    .build()
                            )
                            .collect(Collectors.toList());
                    return IngredientGroupValidate.builder()
                            .id(g.getId())
                            .name(g.getName())
                            .ingredients(ingredients)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private <T> T find(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public long create(ProductCreateCommand command) {
        long stockId = stockCommandService.create(command.getStock());
        Product product = Product.builder()
                .name(command.getName())
                .description(command.getDescription())
                .price(command.getPrice())
                .maxOrderQuantity(command.getMaxOrderQuantity())
                .stockId(stockId)
                .build();
        productRepository.save(product);
        return product.getId();
    }

    private ProductValidate getProductValidate(ProductValidateCommand command) {
        return ProductValidate
                .builder()
                .id(command.getId())
                .name(command.getName())
                .price(command.getPrice())
                .orderQuantity(command.getOrderQuantity())
                .build();
    }
}






















