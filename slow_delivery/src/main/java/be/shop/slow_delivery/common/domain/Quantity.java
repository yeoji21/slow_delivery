package be.shop.slow_delivery.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;


@EqualsAndHashCode(of = "quantity", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Quantity {
    public static Quantity ZERO = new Quantity(0);

    @JsonProperty("quantity")
    private int quantity;

    public Quantity(int quantity) {
        Assert.isTrue(quantity >= 0, "잘못된 수량입니다.");
        this.quantity = quantity;
    }

    public int toInt() {
        return quantity;
    }

    public Quantity plus(Quantity quantity) {
        return new Quantity(this.quantity + quantity.toInt());
    }

    public Quantity minus(Quantity quantity) {
        return new Quantity(this.quantity - quantity.toInt());
    }
}
