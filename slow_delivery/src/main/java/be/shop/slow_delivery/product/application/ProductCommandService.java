package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.product.application.dto.ProductValidationCommand;
import be.shop.slow_delivery.product.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final ProductValidationService productValidationService;

    @Transactional(readOnly = true)
    public void validate(ProductValidationCommand command) {
        Map<IngredientGroup, List<Ingredient>> ingredientsMap =
                productRepository.findIngredientsMap(command.getProductId(), command.getIngredientIds());
        Map<OptionGroup, List<Option>> optionsMap =
                productRepository.findOptionsMap(command.getProductId(), command.getOptionIds());

        productValidationService.validateIngredients(ingredientsMap, command.getIngredientIds());
        productValidationService.validateOptions(optionsMap, command.getOptionIds());
    }

}
