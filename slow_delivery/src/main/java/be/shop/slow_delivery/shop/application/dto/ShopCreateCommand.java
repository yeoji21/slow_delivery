package be.shop.slow_delivery.shop.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ShopCreateCommand {
    private String shopName;
    private int minOrderAmount;
    private String phoneNumber;
    private String streetAddress;
    private String category;
    private String introduction;
    private String openingHours;
    private String dayOff;

    @Builder
    public ShopCreateCommand(String shopName,
                             int minOrderAmount,
                             String phoneNumber,
                             String streetAddress,
                             String category,
                             String introduction,
                             String openingHours,
                             String dayOff) {
        this.shopName = shopName;
        this.minOrderAmount = minOrderAmount;
        this.phoneNumber = phoneNumber;
        this.streetAddress = streetAddress;
        this.category = category;
        this.introduction = introduction;
        this.openingHours = openingHours;
        this.dayOff = dayOff;
    }
}
