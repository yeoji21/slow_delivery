package be.shop.slow_delivery.application.dto;

import be.shop.slow_delivery.shop.domain.Shop;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;


/*
    가게 상세 정보 :
    가게 ID, 가게 이름, 최소 주문 금액, 가게 썸네일 파일 저장 경로, 기본 배달료 리스트,
    소개글, 전화번호, 영업 시간, 휴무일, 도로명 주소
 */
@Getter
public class ShopDetailInfo {
    private long shopId;
    private String shopName;
    private int minOrderAmount;
    private String thumbnailPath;
    private List<Integer> defaultDeliveryFees;
    private String introduction;
    private String phoneNumber;
    private String openingHours;
    private String dayOff;
    private String streetAddress;

    @QueryProjection
    public ShopDetailInfo(Shop shop, String thumbnailPath, List<Integer> defaultDeliveryFees) {
        this.shopId = shop.getId();
        this.shopName = shop.getName();
        this.minOrderAmount = shop.getMinOrderAmount().toInt();
        this.thumbnailPath = thumbnailPath;
        this.defaultDeliveryFees = defaultDeliveryFees;
        this.introduction = shop.getIntroduction();
        this.phoneNumber = shop.getPhoneNumber().toString();
        this.openingHours = shop.getBusinessTimeInfo().getOpeningHours();
        this.dayOff = shop.getBusinessTimeInfo().getDayOff();
        this.streetAddress = shop.getLocation().getStreetAddress();
    }
}
