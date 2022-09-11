package be.shop.slow_delivery.seller.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "authority")
public class Authority {

    @Id @Column(name = "authority_name")
    private String authorityName;

    @Builder
    public Authority(String authorityName){
        this.authorityName = authorityName;
    }
}
