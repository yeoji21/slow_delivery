package be.shop.slow_delivery.shop.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.Objects;

@Getter
public class DeliveryFeeInfo {
    private int orderAmount;
    private int deliveryFee;

    @QueryProjection
    public DeliveryFeeInfo(int orderAmount, int deliveryFee) {
        this.orderAmount = orderAmount;
        this.deliveryFee = deliveryFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryFeeInfo that = (DeliveryFeeInfo) o;
        return getOrderAmount() == that.getOrderAmount() && getDeliveryFee() == that.getDeliveryFee();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderAmount(), getDeliveryFee());
    }
}
