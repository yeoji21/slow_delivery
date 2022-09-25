package be.shop.slow_delivery.seller.presentation.dto;

import lombok.Data;

@Data
public class LoginSellerCriteria {

    private String token;
    private Long id;
    private String loginId;
    private String email;

    public LoginSellerCriteria(String token, Long id, String loginId, String email){
        this.token = token;
        this.id = id;
        this.loginId = loginId;
        this.email = email;
    }
}
