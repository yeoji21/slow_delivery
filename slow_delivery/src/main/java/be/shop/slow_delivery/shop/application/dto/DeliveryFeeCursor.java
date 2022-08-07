package be.shop.slow_delivery.shop.application.dto;

import lombok.Getter;

@Getter
public class DeliveryFeeCursor {
    public static final DeliveryFeeCursor EMPTY = new DeliveryFeeCursor(null, null);

    private Integer Fee;
    private Long shopId;

    public DeliveryFeeCursor(Integer Fee, Long shopId) {
        this.Fee = Fee;
        this.shopId = shopId;
    }
}
