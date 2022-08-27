package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.dto.ProductValidationCommand;
import be.shop.slow_delivery.product.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductCommandServiceTest {
    @Mock private ProductRepository productRepository;
    @Mock private ProductValidationService productValidationService;
    @InjectMocks private ProductCommandService productCommandService;

    @Test
    void 상품_필수_선택_옵션_검증() throws Exception{
        //given
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();

        ProductValidationCommand command = ProductValidationCommand.builder()
                .productId(1L)
                .orderQuantity(new Quantity(3))
                .ingredientIds(List.of(1L, 2L, 3L))
                .optionIds(List.of(1L, 2L))
                .build();
        Map<IngredientGroup, List<Ingredient>> ingredientsMap = new HashMap<>();
        Map<OptionGroup, List<Option>> optionsMap = new HashMap<>();

        given(productRepository.findById(command.getProductId())).willReturn(Optional.ofNullable(product));
        given(productRepository.findIngredientsMap(command.getProductId(), command.getIngredientIds())).willReturn(ingredientsMap);
        given(productRepository.findOptionsMap(command.getProductId(), command.getOptionIds())).willReturn(optionsMap);

        //when
        productCommandService.validate(command);

        //then
        verify(productValidationService).validateProduct(product, command.getOrderQuantity());
        verify(productValidationService).validateIngredients(ingredientsMap, command.getIngredientIds());
        verify(productValidationService).validateOptions(optionsMap, command.getOptionIds());
    }
}