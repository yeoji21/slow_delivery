package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.PhoneNumber;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@SecondaryTable(
        name = "shop_location",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "shop_id", referencedColumnName = "shop_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shop")
public class Shop extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
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

    @ElementCollection
    @CollectionTable(
            name = "shop_category",
            joinColumns = @JoinColumn(name = "shop_id", referencedColumnName = "shop_id")
    )
    @Column(name = "category_id")
    private Set<Long> categoryIds;

    @Builder
    public Shop(Long id,
                String name,
                Money minOrderAmount,
                PhoneNumber phoneNumber,
                String introduction,
                BusinessTimeInfo businessTimeInfo,
                ShopLocation location,
                Long shopThumbnailFileId,
                @Singular Set<Long> categoryIds) {
        this.id = id;
        this.name = name;
        this.minOrderAmount = minOrderAmount;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.businessTimeInfo = businessTimeInfo;
        this.location = location;
        this.shopThumbnailFileId = shopThumbnailFileId;
        this.categoryIds = categoryIds;
        if(categoryIds.size() == 0) throw new IllegalArgumentException();
    }

    public void updateShopThumbnail(Long fileId) {
        this.shopThumbnailFileId = fileId;
    }
}
