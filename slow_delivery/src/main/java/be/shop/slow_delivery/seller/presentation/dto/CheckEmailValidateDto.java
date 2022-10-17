package be.shop.slow_delivery.seller.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckEmailValidateDto {
    private String emailAddress;
    private String code;

    @Builder
    public CheckEmailValidateDto(String emailAddress, String code) {
        this.emailAddress = emailAddress;
        this.code = code;
    }
}
