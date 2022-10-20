package be.shop.slow_delivery.seller.domain;

import be.shop.slow_delivery.common.domain.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seller")
public class Seller {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Embedded
    private PhoneNumber phoneNumber;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "login_id",nullable = false, unique = true)
    private String loginId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Builder
    public Seller(String loginId,
                  String password,
                  String email,
                  String phoneNumber,
                  String username,
                  SellerRole role){
        this.username = username;
        this.email = email;
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.loginId = loginId;
        this.password = password;
        this.role = role.getRole();
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
