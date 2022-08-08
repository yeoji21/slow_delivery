package be.shop.slow_delivery.application.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ShopListQueryResult {
    private List<ShopSimpleInfo> shopList;
    private boolean hasNext;
    private String nextCursor;

    public ShopListQueryResult(List<ShopSimpleInfo> shopList, boolean hasNext, String nextCursor) {
        this.shopList = shopList;
        this.hasNext = hasNext;
        this.nextCursor = nextCursor;
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
