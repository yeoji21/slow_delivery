package be.shop.slow_delivery.shop.application.dto;

import be.shop.slow_delivery.common.domain.EmbeddedType;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.shop.domain.BusinessTimeInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShopInfoModifyCommand {
    private Money minOrderAmount;
    private String description;
    @EmbeddedType
    private BusinessTimeInfo businessTimeInfo;

    @Builder
    public ShopInfoModifyCommand(Integer minOrderAmount,
                                 String description,
                                 String openingHours,
                                 String dayOff) {
        this.minOrderAmount = minOrderAmount == null ? null : new Money(minOrderAmount);
        this.description = description;
        this.businessTimeInfo = new BusinessTimeInfo(openingHours, dayOff);
    }
}
