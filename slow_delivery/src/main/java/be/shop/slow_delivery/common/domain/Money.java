package be.shop.slow_delivery.common.domain;


import be.shop.slow_delivery.exception.InvalidValueException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static be.shop.slow_delivery.exception.ErrorCode.MONEY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode(of = "value")
public class Money {
    public static final Money ZERO = new Money(0);
    private int value;

    public Money(int value) {
        if(value < 0)
            throw new InvalidValueException(MONEY);
        this.value = value;
    }

    public Money add(Money money) {
        return new Money(this.value + money.value);
    }

    public Money minus(Money money) {
        return new Money(this.value - money.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int toInt() {
        return value;
    }
}