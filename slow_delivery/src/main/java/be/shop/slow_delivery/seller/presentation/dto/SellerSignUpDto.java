package be.shop.slow_delivery.seller.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@NoArgsConstructor
@Getter
public class SellerSignUpDto {

    @NotEmpty
    private String username;

    @NotEmpty
    @Size(min = 3, max = 50)
    private String loginId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    @Size(min = 3, max = 50)
    private String password;

    @Email
    @Size(min=3, max=50)
    private String email;

    @NotEmpty
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
