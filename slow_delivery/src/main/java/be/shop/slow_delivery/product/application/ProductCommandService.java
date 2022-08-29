package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.exception.ErrorCode;
import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.product.application.dto.ProductPlaceCommand;
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
    private final ProductPlaceOrderService placeOrderService;

    @Transactional(readOnly = true)
    public Money placeOrder(ProductPlaceCommand command) {
        Product product = productRepository.findById(command.getProductId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        Map<IngredientGroup, List<Ingredient>> ingredientsMap = findIngredientsMap(command);
        Map<OptionGroup, List<Option>> optionsMap = findOptionGroupListMap(command);

        return placeOrderService.place(product, ingredientsMap, optionsMap, command);
    }

    private Map<IngredientGroup, List<Ingredient>> findIngredientsMap(ProductPlaceCommand command) {
        return productRepository.findIngredientsMap(command.getProductId(), command.getIngredientIds());
    }

    private Map<OptionGroup, List<Option>> findOptionGroupListMap(ProductPlaceCommand command) {
        return productRepository.findOptionsMap(command.getProductId(), command.getOptionIds());
    }

}
