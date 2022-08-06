package be.shop.slow_delivery.shop.application.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ShopListQueryResult {
    private List<ShopSimpleInfo> shopList;
    private boolean hasNext;

    public ShopListQueryResult(List<ShopSimpleInfo> shopList, boolean hasNext) {
        this.shopList = shopList;
        this.hasNext = hasNext;
    }
}
