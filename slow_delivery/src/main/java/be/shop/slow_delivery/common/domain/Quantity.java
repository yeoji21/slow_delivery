package be.shop.slow_delivery.common.domain;

import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Quantity {
    public static Quantity ZERO = new Quantity(0);
    private int quantity;

    public Quantity(int quantity) {
        Assert.isTrue(quantity >= 0, "잘못된 수량입니다.");
        this.quantity = quantity;
    }

    public int toInt() {
        return quantity;
    }

    public Quantity plus(Quantity quantity) {
        return new Quantity(this.quantity + quantity.quantity);
    }

    public Quantity minus(Quantity quantity) {
        return new Quantity(this.quantity - quantity.quantity);
    }
}
