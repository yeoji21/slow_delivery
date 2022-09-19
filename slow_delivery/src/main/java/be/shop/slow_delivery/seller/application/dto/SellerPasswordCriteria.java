package be.shop.slow_delivery.seller.application.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SellerPasswordCriteria {
    @NotBlank
    private String password;
}
