package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.common.domain.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shop")
public class Shop {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "thumbnail_file_id")
    private Long shopThumbnailFileId;

    @Embedded @Column(name = "min_order_price")
    private Money minOrderAmount;

    @Embedded
    private DefaultDeliveryFees defaultDeliveryFees;

    @Builder
    public Shop(String name,
                Long shopThumbnailFileId,
                Money minOrderAmount) {
        this.name = name;
        this.shopThumbnailFileId = shopThumbnailFileId;
        this.minOrderAmount = minOrderAmount;
        this.defaultDeliveryFees = new DefaultDeliveryFees();
    }

    public void addDeliveryFeeOption(OrderAmountDeliveryFee deliveryFee) {
        defaultDeliveryFees.add(deliveryFee);
        deliveryFee.setShop(this);
    }
}
