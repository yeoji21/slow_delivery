package be.shop.slow_delivery.shop.application.dto;


import be.shop.slow_delivery.shop.domain.Shop;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/*
    가게 간략 정보 :
    가게 ID, 가게 이름, 최소 주문 금액, 가게 썸네일 파일 저장 경로, 기본 배달료 리스트
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopSimpleInfo {
    private long shopId;
    private String shopName;
    private int minOrderAmount;
    private String thumbnail;
    private List<Integer> defaultDeliveryFees;

    @QueryProjection
    public ShopSimpleInfo(Shop shop, String thumbnailPath) {
        this.shopId = shop.getId();
        this.shopName = shop.getName();
        this.minOrderAmount = shop.getMinOrderAmount().toInt();
        this.thumbnail = thumbnailPath;
        this.defaultDeliveryFees = new ArrayList<>();
    }

    public void setDefaultDeliveryFees(List<Integer> defaultDeliveryFees) {
        this.defaultDeliveryFees.addAll(defaultDeliveryFees);
    }
}
