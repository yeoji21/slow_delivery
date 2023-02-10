package be.shop.slow_delivery.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneNumberTest {
    @Test
    @DisplayName("올바른 전화 번호")
    void correct_phone_number() throws Exception{
        isCorrectPhoneNumber("010-1234-5678");
        isCorrectPhoneNumber("02-1234-5678");
        isCorrectPhoneNumber("02-123-5678");
    }

    @Test
    void incorrect_phone_number() throws Exception{
        isCorrectPhoneNumber("010-1234-5678");
    }

    private void isCorrectPhoneNumber(String phoneNumber) {
        assertThat(new PhoneNumber(phoneNumber).getPhoneNumber()).isEqualTo(phoneNumber);
    }
}