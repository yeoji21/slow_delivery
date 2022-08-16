package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class IngredientUnitTest {

    @Test
    void 상품구성_저장() throws Exception{
        Ingredient.builder()
                .name("ingredient A")
                .price(new Money(500))
                .stockId(1L)
                .build();
    }

    @Test
    void 상품구성_저장_예외() throws Exception{
        assertThrows(IllegalArgumentException.class,
                () -> Ingredient.builder()
                        .price(new Money(500))
                        .stockId(1L)
                        .build());

        assertThrows(IllegalArgumentException.class,
                () -> Ingredient.builder()
                        .name("ingredient A")
                        .stockId(1L)
                        .build());

        assertThrows(IllegalArgumentException.class,
                () -> Ingredient.builder()
                        .name("ingredient A")
                        .price(new Money(500))
                        .build());
    }
}