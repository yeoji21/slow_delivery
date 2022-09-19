package be.shop.slow_delivery.seller.application.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SellerPasswordCommand {
    @NotBlank
    private String password;
}
