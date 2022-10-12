package be.shop.slow_delivery.seller.presentation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailValidateDto {
    @Email
    private String emailAddress;

    public EmailValidateDto(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
