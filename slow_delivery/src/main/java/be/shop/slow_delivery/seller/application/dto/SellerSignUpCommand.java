package be.shop.slow_delivery.seller.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SellerSignUpCommand {
    private String username;
    private String loginId;
    private String password;
    private String email;
    private String phoneNumber;

    @Builder
    public SellerSignUpCommand(String username,
                               String loginId,
                               String password,
                               String email,
                               String phoneNumber) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
