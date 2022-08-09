package be.shop.slow_delivery.common.domain;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode(of = "value")
public class Money {
    public static final Money EMPTY = new Money(0);
    private int value;

    public Money(int value) {
        // TODO: 2022/08/09 exception
        if(value < 0) throw new IllegalArgumentException();
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