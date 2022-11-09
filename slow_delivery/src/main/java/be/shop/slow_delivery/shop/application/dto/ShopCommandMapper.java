package be.shop.slow_delivery.shop.application.dto;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopModifyDomain;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ShopCommandMapper {
    ShopCommandMapper INSTANCE = Mappers.getMapper(ShopCommandMapper.class);

    @Mappings({
            @Mapping(target = "name", source = "command.shopName"),
            @Mapping(target = "minOrderAmount", source = "command.minOrderAmount"),
            @Mapping(target = "location.streetAddress", source = "command.streetAddress"),
            @Mapping(target = "category", source = "category")
    })
    Shop toShop(ShopCreateCommand command, Category category);

    ShopModifyDomain toDomain(ShopInfoModifyCommand command);

    default Money toMoney(int minOrderAmount){
        return new Money(minOrderAmount);
    }
}
