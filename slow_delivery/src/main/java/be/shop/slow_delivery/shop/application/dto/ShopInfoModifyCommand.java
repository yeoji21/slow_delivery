package be.shop.slow_delivery.shop.application.dto;

import be.shop.slow_delivery.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShopInfoModifyCommand {
    private long shopId;
    private Money minOrderPrice;
    private String description;
    private String openingHours;
    private String dayOff;

    @Builder
    public ShopInfoModifyCommand(long shopId,
                                 int minOrderPrice,
                                 String description,
                                 String openingHours,
                                 String dayOff) {
        this.shopId = shopId;
        this.minOrderPrice = new Money(minOrderPrice);
        this.description = description;
        this.openingHours = openingHours;
        this.dayOff = dayOff;
    }
}
