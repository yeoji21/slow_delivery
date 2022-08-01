package be.shop.slow_delivery.shop.application.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/*
    가게 간략 정보 :
    가게 ID, 가게 이름, 최소 주문 금액, 가게 썸네일 파일 저장 경로, 기본 배달료
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopSimpleInfo {
    private long shopId;
    private String shopName;
    private int minOrderAmount;
    private String thumbnail;
    private int deliveryFee;
}
