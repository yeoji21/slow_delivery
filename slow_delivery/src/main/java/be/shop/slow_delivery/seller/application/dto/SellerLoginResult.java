package be.shop.slow_delivery.seller.application.dto;

import lombok.Getter;

@Getter
public class SellerLoginResult {
    private String token;

    public SellerLoginResult(String token) {
        this.token = token;
    }
}
