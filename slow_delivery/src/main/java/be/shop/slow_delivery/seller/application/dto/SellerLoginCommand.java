package be.shop.slow_delivery.seller.application.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class SellerLoginCommand {
    @NotNull
    @Size(min =3, max =50)
    private String loginId;
    @NotNull
    @Size(min =3, max =50)
    private String password;
}
