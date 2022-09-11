package be.shop.slow_delivery.seller.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class SellerDto {

    @NotEmpty
    private String sellerId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    private String password;

    @NotEmpty
    private String email;
}
