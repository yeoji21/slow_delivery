package be.shop.slow_delivery.shop.application.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ShopListQueryResult {
    private List<ShopSimpleInfo> shopList;

    public ShopListQueryResult(List<ShopSimpleInfo> shopList) {
        this.shopList = shopList;
    }
}
