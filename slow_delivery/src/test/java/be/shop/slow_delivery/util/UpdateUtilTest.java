package be.shop.slow_delivery.util;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.category.domain.CategoryType;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.shop.application.dto.ShopInfoModifyCommand;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopLocation;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpdateUtilTest {

    @Test
    void notExistField() throws Exception{
        //given
        Shop shop = getShop();
        DummyDto dto = new DummyDto();

        //when then
        assertThrows(IllegalArgumentException.class, () -> UpdateUtil.update(shop, dto));
    }

    @Test
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

    @Test
    void update_with_null() throws Exception{
        //given
        Shop shop = getShop();
        ShopInfoModifyCommand command = ShopInfoModifyCommand.builder()
                .minOrderAmount(10_000)
                .openingHours("updated openingHours")
                .dayOff("updated dayOff")
                .build();

        //when
        UpdateUtil.update(shop, command);

        //then
        assertThat(shop.getMinOrderAmount()).isEqualTo(new Money(10_000));
        assertThat(shop.getDescription()).isNotEqualTo(command.getDescription());
        assertThat(shop.getBusinessTimeInfo().getOpeningHours()).isEqualTo(command.getBusinessTimeInfo().getOpeningHours());
        assertThat(shop.getBusinessTimeInfo().getDayOff()).isEqualTo(command.getBusinessTimeInfo().getDayOff());
    }

    @Test
    void update_with_null2() throws Exception{
        //given
        Shop shop = getShop();
        ShopInfoModifyCommand command = ShopInfoModifyCommand.builder()
                .description("updated description")
                .minOrderAmount(10_000)
                .dayOff("updated dayOff")
                .build();

        //when
        System.out.println(shop.getBusinessTimeInfo().getOpeningHours());
        UpdateUtil.update(shop, command);
        System.out.println(shop.getBusinessTimeInfo().getOpeningHours());

        //then
        assertThat(shop.getMinOrderAmount()).isEqualTo(new Money(10_000));
        assertThat(shop.getDescription()).isEqualTo(command.getDescription());
        assertThat(command.getBusinessTimeInfo().getOpeningHours()).isNull();
        assertThat(shop.getBusinessTimeInfo().getOpeningHours()).isNotNull();
        assertThat(shop.getBusinessTimeInfo().getDayOff()).isEqualTo(command.getBusinessTimeInfo().getDayOff());
    }

    @Test
    void update_with_null3() throws Exception{
        //given
        Shop shop = getShop();
        ShopInfoModifyCommand command = ShopInfoModifyCommand.builder()
                .openingHours("updated openingHours")
                .description("updated description")
                .dayOff("updated dayOff")
                .build();

        //when
        System.out.println(shop.getMinOrderAmount().toInt());
        UpdateUtil.update(shop, command);
        System.out.println(shop.getMinOrderAmount().toInt());

        //then
        assertThat(shop.getMinOrderAmount()).isNotEqualTo(new Money(10_000));
        assertThat(shop.getDescription()).isEqualTo(command.getDescription());
        assertThat(shop.getBusinessTimeInfo().getOpeningHours()).isEqualTo(command.getBusinessTimeInfo().getOpeningHours());
        assertThat(shop.getBusinessTimeInfo().getDayOff()).isEqualTo(command.getBusinessTimeInfo().getDayOff());
    }

    static class UpdateUtil{

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
                    System.out.println(dtoValue);
                    if(dtoValue == null) {
                        System.out.println(dtoField.getName());
                        continue;
                    }
                    entityField.set(entity, dtoValue);
                }
            } catch (Exception e) {
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
