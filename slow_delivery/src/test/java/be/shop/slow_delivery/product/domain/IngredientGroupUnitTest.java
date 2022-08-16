package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.Money;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IngredientGroupUnitTest {
    @Test
    void 상품구성그룹_저장() throws Exception{
        IngredientGroup.builder()
                .name("groupA")
                .selectCount(new SelectCount(1, 1))
                .build();
    }

    @Test
    void 상품구성그룹_저장_예외() throws Exception{
        assertThrows(IllegalArgumentException.class,
                () -> IngredientGroup.builder()
                        .selectCount(new SelectCount(1, 1))
                        .build());

        assertThrows(IllegalArgumentException.class,
                () -> IngredientGroup.builder()
                        .name("groupA")
                        .build());

        assertThrows(IllegalArgumentException.class,
                () -> IngredientGroup.builder()
                        .name("groupA")
                        .selectCount(new SelectCount(2, 1))
                        .build());
    }

    @Test
    void 상품구성_추가() throws Exception{
        //given
        IngredientGroup ingredientGroup = IngredientGroup.builder()
                .name("groupA")
                .selectCount(new SelectCount(1, 1))
                .build();

        Ingredient ingredient = Ingredient.builder()
                .name("ingredient")
                .price(new Money(1_000))
                .stockId(1L)
                .build();

        //when
        ingredientGroup.addIngredient(ingredient);

        //then
        assertThat(ingredientGroup.getIngredients().size()).isEqualTo(1);
    }
}