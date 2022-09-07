package be.shop.slow_delivery.seller.domain;

import be.shop.slow_delivery.common.domain.PhoneNumber;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seller")
public class Seller {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Embedded
    private PhoneNumber phoneNumber;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "login_id",nullable = false, unique = true)
    private String loginId;

    @Column(name = "password")
    private String password;
}
