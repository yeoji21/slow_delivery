package be.shop.slow_delivery.shop.presentation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShopOrderType {
    NEWEST("newest"),
    DELIVERY_FEE("delivery-fee");

    private final String type;

    public static ShopOrderType of(String order) {
        for (ShopOrderType value : ShopOrderType.values()) {
            if(value.type.equals(order)) return value;
        }
        throw new IllegalArgumentException("지원하지 않는 정렬 타입입니다.");
    }
}
