package be.shop.slow_delivery.util;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.category.domain.CategoryType;
import be.shop.slow_delivery.common.domain.EmbeddedType;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.shop.application.dto.ShopInfoModifyCommand;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopLocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpdateUtilTest {

    @Test @DisplayName("존재하지 않는 필드에 대한 수정 시도")
    void notExistField() throws Exception{
        //given
        Shop shop = getShop();
        DummyDto dto = new DummyDto();

        //when then
        assertThrows(IllegalArgumentException.class, () -> UpdateUtil.update(shop, dto));
    }

    @Test @DisplayName("모든 값을 채운 수정 요청")
    void update() throws Exception{
        //given
        Shop shop = getShop();
        ShopInfoModifyCommand command = ShopInfoModifyCommand.builder()
                .minOrderAmount(10_000)
                .description("updated description")
                .openingHours("updated openingHours")
                .dayOff("updated dayOff")
                .build();

        //when
        UpdateUtil.update(shop, command);

        //then
        assertThat(shop.getMinOrderAmount()).isEqualTo(new Money(10_000));
        assertThat(shop.getDescription()).isEqualTo(command.getDescription());
        assertThat(shop.getBusinessTimeInfo().getOpeningHours()).isEqualTo(command.getBusinessTimeInfo().getOpeningHours());
        assertThat(shop.getBusinessTimeInfo().getDayOff()).isEqualTo(command.getBusinessTimeInfo().getDayOff());
    }

    @Test @DisplayName("영업 시간은 변경하지 않음")
    void update_with_null() throws Exception{
        //given
        Shop shop = getShop();
        ShopInfoModifyCommand command = ShopInfoModifyCommand.builder()
                .description("updated description")
                .minOrderAmount(10_000)
                .dayOff("updated dayOff")
                .build();

        //when
        UpdateUtil.update(shop, command);

        //then
        assertThat(shop.getMinOrderAmount()).isEqualTo(new Money(10_000));
        assertThat(shop.getDescription()).isEqualTo(command.getDescription());
        assertThat(command.getBusinessTimeInfo().getOpeningHours()).isNull();
        assertThat(shop.getBusinessTimeInfo().getOpeningHours()).isNotNull();
        assertThat(shop.getBusinessTimeInfo().getDayOff()).isEqualTo(command.getBusinessTimeInfo().getDayOff());
    }

    @Test @DisplayName("최소 주문 금액은 변경하지 않음")
    void update_with_null3() throws Exception{
        //given
        Shop shop = getShop();
        ShopInfoModifyCommand command = ShopInfoModifyCommand.builder()
                .description("updated description")
                .openingHours("updated openingHours")
                .dayOff("updated dayOff")
                .build();
        Money originalAmount = shop.getMinOrderAmount();

        //when
        UpdateUtil.update(shop, command);

        //then
        assertThat(shop.getMinOrderAmount()).isEqualTo(originalAmount);
        assertThat(shop.getDescription()).isEqualTo(command.getDescription());
        assertThat(shop.getBusinessTimeInfo().getOpeningHours()).isEqualTo(command.getBusinessTimeInfo().getOpeningHours());
        assertThat(shop.getBusinessTimeInfo().getDayOff()).isEqualTo(command.getBusinessTimeInfo().getDayOff());
    }

    static class UpdateUtil{
        // TODO: 2023/02/10 null로 변경하고 싶은 거라면?
        protected static <E, D> void update(E entity, D dto) {
            try {
                for (Field dtoField : dto.getClass().getDeclaredFields()) {
                    Field entityField = entity.getClass().getDeclaredField(dtoField.getName());
                    if(!entityField.getType().equals(dtoField.getType())){
                        throw new IllegalArgumentException("필드의 타입이 다릅니다.");
                    }
                    dtoField.setAccessible(true);
                    entityField.setAccessible(true);
                    Object dtoValue = dtoField.get(dto);
                    if(dtoValue == null) continue;
                    if(dtoField.isAnnotationPresent(EmbeddedType.class)){
                        update(entityField.get(entity), dtoValue);
                    }
                    else {
                        entityField.set(entity, dtoValue);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new IllegalArgumentException(e.getCause());
            }
        }
    }

    static class DummyDto{
        private int notExistField;
    }

    private Shop getShop() {
        Category category = new Category(CategoryType.CHICKEN);
        ReflectionTestUtils.setField(category, "id", 1L);
        return Shop.builder()
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
}
