package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shop")
public class Shop extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "thumbnail_file_id")
    private Long shopThumbnailFileId;

    @Embedded @Column(name = "min_order_price")
    private Money minOrderAmount;

    // 양방향 관계 끊는 것도 고려
    @Embedded
    private DefaultDeliveryFeeOptions defaultDeliveryFeeOptions;

    @Builder
    public Shop(String name,
                Long shopThumbnailFileId,
                Money minOrderAmount,
                List<OrderAmountDeliveryFee> deliveryFees) {
        this.name = name;
        this.shopThumbnailFileId = shopThumbnailFileId;
        this.minOrderAmount = minOrderAmount;
        this.defaultDeliveryFeeOptions = new DefaultDeliveryFeeOptions();
        if(deliveryFees == null || deliveryFees.size() < 1)
            throw new IllegalArgumentException("delivery fee is essential");
        deliveryFees.forEach(this::addDeliveryFeeOption);
    }

    public void setShopThumbnail(Long fileId) {
        this.shopThumbnailFileId = fileId;
    }

    public void addDeliveryFeeOption(OrderAmountDeliveryFee deliveryFee) {
        defaultDeliveryFeeOptions.add(deliveryFee);
        deliveryFee.setShop(this);
    }

    public List<Money> getDefaultDeliveryFees() {
        return defaultDeliveryFeeOptions
                .getDeliveryFeeOptions()
                .stream()
                .map(OrderAmountDeliveryFee::getFee)
                .collect(Collectors.toList());
    }
}
