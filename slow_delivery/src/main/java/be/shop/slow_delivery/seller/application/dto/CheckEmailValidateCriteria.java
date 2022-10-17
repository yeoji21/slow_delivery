package be.shop.slow_delivery.seller.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CheckEmailValidateCriteria {
    private String emailAddress;
    private String code;

    @Builder
    public CheckEmailValidateCriteria(String emailAddress, String code) {
        this.emailAddress = emailAddress;
        this.code = code;
    }
}
