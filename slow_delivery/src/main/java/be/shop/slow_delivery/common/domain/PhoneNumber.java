package be.shop.slow_delivery.common.domain;

import be.shop.slow_delivery.exception.InvalidValueException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static be.shop.slow_delivery.exception.ErrorCode.PHONE_NUMBER;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PhoneNumber {
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        if(!phoneNumber.matches("\\d{2,3}-\\d{3,4}-\\d{4}"))
            throw new InvalidValueException(PHONE_NUMBER);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return phoneNumber;
    }
}
