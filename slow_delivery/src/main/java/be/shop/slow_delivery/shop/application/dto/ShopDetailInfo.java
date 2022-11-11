package be.shop.slow_delivery.shop.application.dto;

import be.shop.slow_delivery.shop.domain.Shop;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ShopDetailInfo {
    private Long shopId;
    private String thumbnailPath;
    private Long areaId;
    private String shopName;
    private int minOrderAmount;
    private String phoneNumber;
    private String description;
    private String openingHours;
    private String dayOff;
    private String streetAddress;
    private boolean openStatus;
    private List<String> categories;
    private List<DeliveryFeeInfo> deliveryFees;

    @QueryProjection
    public ShopDetailInfo(Shop shop, String thumbnailPath) {
        this.shopId = shop.getId();
        this.thumbnailPath = thumbnailPath;
        this.areaId = shop.getLocation().getAreaId();
        this.shopName = shop.getName();
        this.minOrderAmount = shop.getMinOrderAmount().toInt();
        this.description = shop.getDescription();
        this.phoneNumber = shop.getPhoneNumber().toString();
        this.openingHours = shop.getBusinessTimeInfo().getOpeningHours();
        this.dayOff = shop.getBusinessTimeInfo().getDayOff();
        this.streetAddress = shop.getLocation().getStreetAddress();
        this.openStatus = shop.isOpen();
    }

    public void setDeliveryFees(List<DeliveryFeeInfo> deliveryFees) {
        this.deliveryFees = deliveryFees;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Builder
    public ShopDetailInfo(Shop shop,
                          String thumbnailPath,
                          List<DeliveryFeeInfo> deliveryFees,
                          List<String> categories) {
        this(shop, thumbnailPath);
        this.deliveryFees = deliveryFees;
        this.categories = categories;
    }
}
