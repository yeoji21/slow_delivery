package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShopModifyDomain {
    private Money minOrderAmount;
    private String description;
    private BusinessTimeInfo businessTimeInfo;

    @Builder
    public ShopModifyDomain(Money minOrderAmount,
                            String description,
                            BusinessTimeInfo businessTimeInfo) {
        this.minOrderAmount = minOrderAmount;
        this.description = description;
        this.businessTimeInfo = businessTimeInfo;
    }
}
