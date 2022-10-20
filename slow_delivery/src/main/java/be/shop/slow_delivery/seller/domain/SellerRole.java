package be.shop.slow_delivery.seller.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SellerRole {
    USER("ROLE_USER");

    private final String role;
}
