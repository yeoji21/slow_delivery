package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductUnitTest {

    @Test
    void 상품_생성() throws Exception{
        Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();
    }

    @Test
    void 상품_생성_예외발생() throws Exception{
        // 재고 ID가 없는 경우
        assertThrows(IllegalArgumentException.class,
                () -> Product.builder()
                        .name("productA")
                        .description("~~~")
                        .price(new Money(10_000))
                        .maxOrderQuantity(new Quantity(5))
                        .build());

        // 상품명이 없는 경우
        assertThrows(IllegalArgumentException.class,
                () -> Product.builder()
                        .stockId(1L)
                        .description("~~~")
                        .price(new Money(10_000))
                        .maxOrderQuantity(new Quantity(5))
                        .build());

        // 가격이 없는 경우
        assertThrows(IllegalArgumentException.class,
                () -> Product.builder()
                        .stockId(1L)
                        .name("productA")
                        .description("~~~")
                        .maxOrderQuantity(new Quantity(5))
                        .build());

        // 최대 주문 수량이 없는 경우
        assertThrows(IllegalArgumentException.class,
                () -> Product.builder()
                        .stockId(1L)
                        .name("productA")
                        .description("~~~")
                        .price(new Money(10_000))
                        .build());
    }

    @Test
    void 상품구성그룹_추가() throws Exception{
        //given
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();

        IngredientGroup ingredientGroup = IngredientGroup.builder()
                .name("group A")
                .selectCount(new SelectCount(1, 1))
                .build();

        //when
        product.addIngredientGroup(ingredientGroup);

        //then
        assertThat(product.getIngredientGroups().size()).isEqualTo(1);
    }
}