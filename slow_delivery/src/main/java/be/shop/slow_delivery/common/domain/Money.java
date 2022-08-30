package be.shop.slow_delivery.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode(of = "amount")
public class Money {
    public static final Money ZERO = new Money(0);
    @JsonProperty("amount")
    private int amount;

    public Money(int amount) {
        Assert.isTrue(amount >= 0, "잘못된 금액입니다.");
        this.amount = amount;
    }

    public Money add(Money money) {
        return new Money(this.amount + money.amount);
    }

    public Money minus(Money money) {
        return new Money(this.amount - money.amount);
    }

    public Money multiple(Quantity quantity) {
        return new Money(amount * quantity.toInt());
    }

    @Override
    public String toString() {
        return String.valueOf(amount);
    }

    public int toInt() {
        return amount;
    }
}