package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import lombok.*;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(exclude = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_amount_delivery_fee")
@Entity
public class OrderAmountDeliveryFee extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter(AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "order_amount", nullable = false))
    private Money orderAmount;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "fee", nullable = false))
    private Money fee;

    @Builder
    public OrderAmountDeliveryFee(Shop shop, Money orderAmount, Money fee) {
        this.shop = shop;
        this.orderAmount = orderAmount;
        this.fee = fee;
    }

    public Long getShopId() {
        return shop.getId();
    }
}
