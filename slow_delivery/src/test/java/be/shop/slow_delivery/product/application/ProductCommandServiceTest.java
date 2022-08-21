package be.shop.slow_delivery.product.application;

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
        ProductValidationCommand command = ProductValidationCommand.builder()
                .productId(1L)
                .ingredientIds(List.of(1L, 2L, 3L))
                .optionIds(List.of(1L, 2L))
                .build();
        Map<IngredientGroup, List<Ingredient>> ingredientsMap = new HashMap<>();
        Map<OptionGroup, List<Option>> optionsMap = new HashMap<>();

        given(productRepository.findIngredientsMap(command.getProductId(), command.getIngredientIds())).willReturn(ingredientsMap);
        given(productRepository.findOptionsMap(command.getProductId(), command.getOptionIds())).willReturn(optionsMap);

        //when
        productCommandService.validate(command);

        //then
        verify(productValidationService).validateIngredients(ingredientsMap, command.getIngredientIds());
        verify(productValidationService).validateOptions(optionsMap, command.getOptionIds());
    }
}