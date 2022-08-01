package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.common.domain.Money;
import lombok.*;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_amount_delivery_fee")
@Entity
public class OrderAmountDeliveryFee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter(AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "order_price"))
    private Money orderAmount;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "fee"))
    private Money fee;

    @Builder
    public OrderAmountDeliveryFee(Money orderAmount, Money fee) {
        this.orderAmount = orderAmount;
        this.fee = fee;
    }

    void setShop(Shop shop) {
        this.shop = shop;
    }
}
