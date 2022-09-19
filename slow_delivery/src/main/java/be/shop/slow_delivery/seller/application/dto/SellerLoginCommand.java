package be.shop.slow_delivery.seller.application.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SellerLoginCommand {
    @NotNull
    private String loginId;
    @NotNull
    private String password;
}
