package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import lombok.*;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shop")
public class Shop extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "thumbnail_file_id", nullable = true)
    private Long shopThumbnailFileId;

    @Embedded @Column(name = "min_order_price", nullable = false)
    private Money minOrderAmount;

    @Builder
    public Shop(String name,
                Long shopThumbnailFileId,
                Money minOrderAmount) {
        this.name = name;
        this.shopThumbnailFileId = shopThumbnailFileId;
        this.minOrderAmount = minOrderAmount;
    }

    public void setShopThumbnail(Long fileId) {
        this.shopThumbnailFileId = fileId;
    }
}
