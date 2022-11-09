package be.shop.slow_delivery.shop.application.dto;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.shop.domain.BusinessTimeInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShopInfoModifyCommand {
    private Money minOrderAmount;
    private String description;
    private BusinessTimeInfo businessTimeInfo;

    @Builder
    public ShopInfoModifyCommand(int minOrderAmount,
                                 String description,
                                 String openingHours,
                                 String dayOff) {
        this.minOrderAmount = new Money(minOrderAmount);
        this.description = description;
        this.businessTimeInfo = new BusinessTimeInfo(openingHours, dayOff);
    }
}
