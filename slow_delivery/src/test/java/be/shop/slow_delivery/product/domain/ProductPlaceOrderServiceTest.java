package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.dto.ProductPlaceCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductPlaceOrderServiceTest {
    private final ProductPlaceOrderService placeOrderService = new ProductPlaceOrderService();

    @Test @DisplayName("상품 주문 검증 성공 테스트")
    void validateProduct_test_v1() throws Exception{
        //given
        ProductPlaceCommand command = ProductPlaceCommand.builder()
                .productId(1L)
                .ingredientIds(List.of(1L, 2L, 3L))
                .optionIds(List.of(1L, 2L, 3L))
                .orderQuantity(new Quantity(1))
                .build();

        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();
        Map<IngredientGroup, List<Ingredient>> ingredientsMap = getIngredientsMap();
        Map<OptionGroup, List<Option>> optionsMap = getOptionsMap();

        //when
        Money totalAmount = placeOrderService.place(product, ingredientsMap, optionsMap, command);

        //then
        assertThat(totalAmount).isEqualTo(getExpectedAmount(command, product, ingredientsMap, optionsMap));
    }

    @Test @DisplayName("판매 중이 아닌 상품")
    void validateProduct_test_v2() throws Exception{
        //given
        ProductPlaceCommand command = ProductPlaceCommand.builder()
                .productId(1L)
                .orderQuantity(new Quantity(1))
                .build();

        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();
        ReflectionTestUtils.setField(product, "isSale", false);


        //when //then
        assertThrows(IllegalArgumentException.class,
                () -> placeOrderService.place(product, null, null, command));
    }

    @Test @DisplayName("최대 주문 수량 이상")
    void validateProduct_test_v3() throws Exception{
        //given
        ProductPlaceCommand command = ProductPlaceCommand.builder()
                .productId(1L)
                .orderQuantity(new Quantity(10))
                .build();
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();

        //when //then
        assertThrows(IllegalArgumentException.class,
                () -> placeOrderService.place(product, null, null, command));
    }

    @Test @DisplayName("필수 옵션을 찾지 못한 경우 예외 발생")
    void validateIngredients_test() throws Exception{
        //given
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();
        // 4L은 map에 담겨있지 않은 ingredient의 id
        ProductPlaceCommand command = ProductPlaceCommand.builder()
                .productId(1L)
                .ingredientIds(List.of(1L, 2L, 3L, 4L))
                .optionIds(List.of(1L, 2L, 3L))
                .orderQuantity(new Quantity(1))
                .build();
        Map<IngredientGroup, List<Ingredient>> ingredientsMap = getIngredientsMap();

        //when //then
        assertThrows(IllegalArgumentException.class,
                () -> placeOrderService.place(product, ingredientsMap, null, command));
    }

    @Test
    void validateOptions_test() throws Exception{
        //given
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();
        Map<IngredientGroup, List<Ingredient>> ingredientsMap = getIngredientsMap();
        Map<OptionGroup, List<Option>> optionsMap = getOptionsMap();
        // 4L은 map에 담겨있지 않은 option의 id
        ProductPlaceCommand command = ProductPlaceCommand.builder()
                .productId(1L)
                .ingredientIds(List.of(1L, 2L, 3L))
                .optionIds(List.of(1L, 2L, 3L, 4L))
                .orderQuantity(new Quantity(1))
                .build();

        //when //then
        assertThrows(IllegalArgumentException.class,
                () -> placeOrderService.place(product, ingredientsMap, optionsMap, command));
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

    private Money getExpectedAmount(ProductPlaceCommand command,
                                    Product product,
                                    Map<IngredientGroup, List<Ingredient>> ingredientsMap,
                                    Map<OptionGroup, List<Option>> optionsMap) {
        Money productPrice = product.getPrice();
        Money ingredientsAmount = ingredientsMap.values()
                .stream()
                .flatMap(Collection::stream)
                .map(Ingredient::getPrice)
                .reduce(Money.ZERO, Money::add);
        Money optionsAmount = optionsMap.values()
                .stream()
                .flatMap(Collection::stream)
                .map(Option::getPrice)
                .reduce(Money.ZERO, Money::add);
        return productPrice.add(ingredientsAmount)
                .add(optionsAmount)
                .multiple(command.getOrderQuantity());
    }
}