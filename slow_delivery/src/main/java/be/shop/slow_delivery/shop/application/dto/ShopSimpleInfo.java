package be.shop.slow_delivery.shop.application.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


/*
    가게 간략 정보 :
    가게 ID, 가게 이름, 최소 주문 금액, 가게 썸네일 파일 저장 경로, 기본 배달료 리스트
 */
@Getter
public class ShopSimpleInfo {
    private long shopId;
    private String shopName;
    private int minOrderAmount;
    private String thumbnailPath;
    private List<Integer> defaultDeliveryFees;

    @QueryProjection @Builder
    public ShopSimpleInfo(long shopId, String shopName, int minOrderAmount, String thumbnailPath, List<Integer> defaultDeliveryFees) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.minOrderAmount = minOrderAmount;
        this.thumbnailPath = thumbnailPath;
        this.defaultDeliveryFees = defaultDeliveryFees;
    }

    @QueryProjection
    public ShopSimpleInfo(long shopId, String shopName, int minOrderAmount, String thumbnailPath) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.minOrderAmount = minOrderAmount;
        this.thumbnailPath = thumbnailPath;
    }

    public void setDefaultDeliveryFees(List<Integer> defaultDeliveryFees) {
        this.defaultDeliveryFees = defaultDeliveryFees;
    }
}
