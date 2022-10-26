package be.shop.slow_delivery.config.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthConstraints {
    HEADER_STRING("Authorization"),
    TOKEN_PREFIX("Bearer ");

    private final String value;
}
