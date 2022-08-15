package be.shop.slow_delivery.common.domain;

import be.shop.slow_delivery.exception.InvalidValueException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static be.shop.slow_delivery.exception.ErrorCode.QUANTITY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Quantity {
    public static Quantity ZERO = new Quantity(0);
    private int quantity;

    public Quantity(int quantity) {
        if(quantity < 0)
            throw new InvalidValueException(QUANTITY);
        this.quantity = quantity;
    }
}
