package be.shop.slow_delivery.shop.application.dto;

import be.shop.slow_delivery.shop.presentation.dto.ShopCreateDto;
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

    public ShopCreateCommand(ShopCreateDto shopCreateDto) {
        this.shopName = shopCreateDto.getShopName();
        this.minOrderAmount = shopCreateDto.getMinOrderAmount();
        this.phoneNumber = shopCreateDto.getPhoneNumber();
        this.streetAddress = shopCreateDto.getStreetAddress();
        this.category = shopCreateDto.getCategory();
        this.introduction = shopCreateDto.getIntroduction();
        this.openingHours = shopCreateDto.getOpeningHours();
        this.dayOff = shopCreateDto.getDayOff();
    }
}
