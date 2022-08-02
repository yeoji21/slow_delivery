package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.PhoneNumber;
import lombok.*;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@SecondaryTable(
        name = "shop_location",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "shop_id", referencedColumnName = "id")
)
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

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private BusinessTimeInfo businessTimeInfo;

    @Embedded
    private ShopLocation location;

    @Builder
    public Shop(String name,
                Money minOrderAmount,
                PhoneNumber phoneNumber,
                String introduction,
                BusinessTimeInfo businessTimeInfo,
                ShopLocation location,
                Long shopThumbnailFileId) {
        this.name = name;
        this.minOrderAmount = minOrderAmount;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.businessTimeInfo = businessTimeInfo;
        this.location = location;
        this.shopThumbnailFileId = shopThumbnailFileId;
    }

    public void setShopThumbnail(Long fileId) {
        this.shopThumbnailFileId = fileId;
    }
}
