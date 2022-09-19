package be.shop.slow_delivery.seller.application.dto;

import lombok.Data;

@Data
public class SellerLoginCriteria {

    private String token;
    private Long id;
    private String loginId;
    private String email;

    public SellerLoginCriteria(String token, Long id, String loginId, String email){
        this.token = token;
        this.id = id;
        this.loginId = loginId;
        this.email = email;
    }
}
