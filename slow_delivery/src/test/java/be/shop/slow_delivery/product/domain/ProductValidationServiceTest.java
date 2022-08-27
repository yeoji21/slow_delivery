package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductValidationServiceTest {
    private final ProductValidationService validationService = new ProductValidationService();

    @Test @DisplayName("상품 검증 성공 테스트")
    void validateProduct_test_v1() throws Exception{
        //given
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();

        //when
        validationService.validateProduct(product, new Quantity(3));

        //then
    }

    @Test @DisplayName("판매 중이 아닌 상품")
    void validateProduct_test_v2() throws Exception{
        //given
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();
        ReflectionTestUtils.setField(product.getStockInfo(), "isSale", false);

        //when
        assertThrows(IllegalArgumentException.class,
                () -> validationService.validateProduct(product, new Quantity(3)));

        //then
    }

    @Test @DisplayName("최대 주문 수량 이상")
    void validateProduct_test_v3() throws Exception{
        //given
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();

        //when
        assertThrows(IllegalArgumentException.class,
                () -> validationService.validateProduct(product, new Quantity(10)));

        //then
    }

    @Test @DisplayName("필수 옵션 검증 성공 테스트")
    void validateIngredients_test_v1() throws Exception{
        //given
        Map<IngredientGroup, List<Ingredient>> ingredientsMap = getIngredientsMap();
        List<Long> ingredientIds = List.of(1L, 2L, 3L);

        //when
        validationService.validateIngredients(ingredientsMap, ingredientIds);

        //then
    }

    @Test @DisplayName("필수 옵션을 찾지 못한 경우 예외 발생")
    void validateIngredients_test_v3() throws Exception{
        //given
        Map<IngredientGroup, List<Ingredient>> ingredientsMap = getIngredientsMap();
        List<Long> ingredientIds = List.of(1L, 2L, 3L, 4L);
        // 4L은 map에 담겨있지 않은 ingredient의 id

        //when
        assertThrows(IllegalArgumentException.class,
                () -> validationService.validateIngredients(ingredientsMap, ingredientIds));

        //then
    }

    @Test @DisplayName("선택 옵션 검증 성공 테스트")
    void validateOptions_test_v1() throws Exception{
        //given
        Map<OptionGroup, List<Option>> optionsMap = getOptionsMap();
        List<Long> optionIds = List.of(1L, 2L, 3L);

        //when
        validationService.validateOptions(optionsMap, optionIds);

        //then
    }

    @Test
    void validateOptions_test_v2() throws Exception{
        //given
        Map<OptionGroup, List<Option>> optionsMap = getOptionsMap();
        List<Long> optionIds = List.of(1L, 2L, 3L, 4L);
        // 4L은 map에 담겨있지 않은 option의 id

        //when
        assertThrows(IllegalArgumentException.class,
                () -> validationService.validateOptions(optionsMap, optionIds));
        //then
    }

    private Map<OptionGroup, List<Option>> getOptionsMap() {
        OptionGroup groupA = new OptionGroup("groupA", new Quantity(5));
        ReflectionTestUtils.setField(groupA, "id", 1L);

        Option option1 = new Option("option1", new Money(1000), 1L);
        ReflectionTestUtils.setField(option1, "id", 1L);
        Option option2 = new Option("option2", new Money(2000), 2L);
        ReflectionTestUtils.setField(option2, "id", 2L);
        Option option3 = new Option("option3", new Money(3000), 3L);
        ReflectionTestUtils.setField(option3, "id", 3L);

        Map<OptionGroup, List<Option>> optionsMap = new HashMap<>();
        optionsMap.put(groupA, List.of(option1, option2, option3));
        return optionsMap;
    }


    private Map<IngredientGroup, List<Ingredient>> getIngredientsMap() {
        IngredientGroup groupA = new IngredientGroup("groupA", new SelectCount(1, 1));
        ReflectionTestUtils.setField(groupA, "id", 1L);
        IngredientGroup groupB = new IngredientGroup("groupB", new SelectCount(1, 2));
        ReflectionTestUtils.setField(groupB, "id", 2L);

        Ingredient ingredient1 = new Ingredient("ingredient_1", new Money(1_000), 1L);
        ReflectionTestUtils.setField(ingredient1, "id", 1L);
        Ingredient ingredient2 = new Ingredient("ingredient_2", new Money(2_000), 2L);
        ReflectionTestUtils.setField(ingredient2, "id", 2L);
        Ingredient ingredient3 = new Ingredient("ingredient_3", new Money(3_000), 3L);
        ReflectionTestUtils.setField(ingredient3, "id", 3L);

        Map<IngredientGroup, List<Ingredient>> ingredientsMap = new HashMap<>();
        ingredientsMap.put(groupA, List.of(ingredient1));
        ingredientsMap.put(groupB, List.of(ingredient2, ingredient3));
        return ingredientsMap;
    }
}