package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.category.domain.CategoryType;
import be.shop.slow_delivery.common.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ShopUnitTest {

    @Test
    void 가게_생성() throws Exception{
        Category category = new Category(CategoryType.CHICKEN);
        ReflectionTestUtils.setField(category, "id", 1L);

        Shop.builder()
                .name("shop A")
                .minOrderAmount(new Money(15_000))
                .phoneNumber("010-1234-5678")
                .description("~~~")
                .openingHours("오후 2시 ~ 익일 새벽 1시")
                .dayOff("연중무휴")
                .location(
                        ShopLocation.builder()
                                .streetAddress("xxx-xxxx")
                                .build())
                .thumbnailFileId(1L)
                .category(category)
                .build();
    }

    @Test
    void 가게_생성_예외발생() throws Exception{
        Category category = new Category(CategoryType.CHICKEN);
        ReflectionTestUtils.setField(category, "id", 1L);

        // 가게 이름이 없는 경우
        assertThrows(IllegalArgumentException.class,
                () -> Shop.builder()
                        .minOrderAmount(new Money(15_000))
                        .phoneNumber("010-1234-5678")
                        .description("~~~")
                        .openingHours("오후 2시 ~ 익일 새벽 1시")
                        .dayOff("연중무휴")
                        .location(
                                ShopLocation.builder()
                                        .streetAddress("xxx-xxxx")
                                        .build())
                        .thumbnailFileId(1L)
                        .category(category)
                        .build());

        // 최소 주문 금액이 없는 경우
        assertThrows(IllegalArgumentException.class,
                () -> Shop.builder()
                        .name("shop A")
                        .phoneNumber("010-1234-5678")
                        .description("~~~")
                        .openingHours("오후 2시 ~ 익일 새벽 1시")
                        .dayOff("연중무휴")
                        .location(
                                ShopLocation.builder()
                                        .streetAddress("xxx-xxxx")
                                        .build())
                        .thumbnailFileId(1L)
                        .category(category)
                        .build());

        // 전화 번호가 없는 경우
        assertThrows(IllegalArgumentException.class,
                () -> Shop.builder()
                        .name("shop A")
                        .minOrderAmount(new Money(15_000))
                        .description("~~~")
                        .openingHours("오후 2시 ~ 익일 새벽 1시")
                        .dayOff("연중무휴")
                        .location(
                                ShopLocation.builder()
                                        .streetAddress("xxx-xxxx")
                                        .build())
                        .thumbnailFileId(1L)
                        .category(category)
                        .build());

        // 가게 위치가 없는 경우
        assertThrows(IllegalArgumentException.class,
                () -> Shop.builder()
                        .name("shop A")
                        .minOrderAmount(new Money(15_000))
                        .phoneNumber("010-1234-5678")
                        .description("~~~")
                        .openingHours("오후 2시 ~ 익일 새벽 1시")
                        .dayOff("연중무휴")
                        .thumbnailFileId(1L)
                        .category(category)
                        .build());

        // 카테고리가 없는 경우
        assertThrows(IllegalArgumentException.class,
                () -> Shop.builder()
                        .name("shop A")
                        .minOrderAmount(new Money(15_000))
                        .phoneNumber("010-1234-5678")
                        .description("~~~")
                        .openingHours("오후 2시 ~ 익일 새벽 1시")
                        .dayOff("연중무휴")
                        .location(
                                ShopLocation.builder()
                                        .streetAddress("xxx-xxxx")
                                        .build())
                        .thumbnailFileId(1L)
                        .build());
    }
}