package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.PhoneNumber;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryShop> categories = new ArrayList<>();


    @Builder
    public Shop(Long id,
                String name,
                Money minOrderAmount,
                PhoneNumber phoneNumber,
                String introduction,
                BusinessTimeInfo businessTimeInfo,
                ShopLocation location,
                Long shopThumbnailFileId,
                @Singular Set<Category> categories) {
        this.id = id;
        this.name = name;
        this.minOrderAmount = minOrderAmount;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.businessTimeInfo = businessTimeInfo;
        this.location = location;
        this.shopThumbnailFileId = shopThumbnailFileId;
        categories.forEach(category -> this.categories.add(new CategoryShop(this, category.getId())));
        if(this.categories.size() == 0) throw new IllegalArgumentException();
    }

    public void updateShopThumbnail(Long fileId) {
        this.shopThumbnailFileId = fileId;
    }

    public void belongToCategory(Category category) {
        categories.add(new CategoryShop(this, category.getId()));
    }
}
