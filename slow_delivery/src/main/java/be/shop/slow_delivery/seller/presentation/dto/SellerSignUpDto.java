package be.shop.slow_delivery.seller.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerSignUpDto {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 3, max = 50)
    private String loginId;

    @NotBlank
    @Size(min = 3, max = 50)
    private String password;

    @Email
    @Size(min=3, max=50)
    private String email;

    @NotBlank
    private String phoneNumber;

    @Builder
    protected SellerSignUpDto(String username,
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
