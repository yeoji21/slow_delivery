package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.PhoneNumber;
import be.shop.slow_delivery.exception.InvalidValueException;
import be.shop.slow_delivery.menu.domain.Menu;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static be.shop.slow_delivery.exception.ErrorCode.CATEGORY_COUNT;

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
    public Shop(String name,
                Money minOrderAmount,
                String phoneNumber,
                String introduction,
                String openingHours,
                String dayOff,
                ShopLocation location,
                Long shopThumbnailFileId,
                Category category) {
        this.name = name;
        this.minOrderAmount = minOrderAmount;
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.introduction = introduction;
        this.businessTimeInfo = new BusinessTimeInfo(openingHours, dayOff);
        this.location = location;
        this.shopThumbnailFileId = shopThumbnailFileId;
        this.categories.add(new CategoryShop(this, category.getId()));
        if(this.categories.size() == 0)
            throw new InvalidValueException(CATEGORY_COUNT);
    }

    public void updateShopThumbnail(Long fileId) {
        this.shopThumbnailFileId = fileId;
    }

    public void belongToCategory(Category category) {
        categories.add(new CategoryShop(this, category.getId()));
    }
}
