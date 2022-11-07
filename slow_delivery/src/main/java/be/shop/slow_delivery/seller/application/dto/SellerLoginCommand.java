package be.shop.slow_delivery.seller.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SellerLoginCommand {
    @NotNull
    @Size(min =3, max =50)
    private String loginId;
    @NotNull
    @Size(min =3, max =50)
    private String password;

    @Builder
    SellerLoginCommand(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
