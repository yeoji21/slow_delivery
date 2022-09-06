package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.exception.ErrorCode;
import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.product.application.dto.ProductCreateCommand;
import be.shop.slow_delivery.product.application.dto.ProductValidateCommand;
import be.shop.slow_delivery.product.domain.*;
import be.shop.slow_delivery.stock.application.StockCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final ProductValidationService validationService;
    private final StockCommandService stockCommandService;

    @Transactional(readOnly = true)
    public Money validateOrder(ProductValidateCommand command) {
        Product product = productRepository.findById(command.getProductId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        Map<IngredientGroup, List<Ingredient>> ingredientsMap = findIngredientsMap(command);
        Map<OptionGroup, List<Option>> optionsMap = findOptionGroupListMap(command);

        return validationService.validate(product, ingredientsMap, optionsMap, command);
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

    private Map<IngredientGroup, List<Ingredient>> findIngredientsMap(ProductValidateCommand command) {
        return productRepository.findIngredientsMap(command.getProductId(), command.getIngredientIds());
    }

    private Map<OptionGroup, List<Option>> findOptionGroupListMap(ProductValidateCommand command) {
        return productRepository.findOptionsMap(command.getProductId(), command.getOptionIds());
    }
}






















