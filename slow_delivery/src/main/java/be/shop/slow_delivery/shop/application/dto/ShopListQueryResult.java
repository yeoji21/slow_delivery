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

    public ShopListQueryResult(List<ShopSimpleInfo> shopList, int size) {
        boolean hasNext = false;
        if (shopList.size() > size) {
            shopList.remove(size);
            hasNext = true;
        }
        this.shopList = shopList;
        this.hasNext = hasNext;
    }
}
