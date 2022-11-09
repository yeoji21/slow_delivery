package be.shop.slow_delivery.shop.domain;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.PhoneNumber;
import be.shop.slow_delivery.exception.InvalidValueException;
import com.mysema.commons.lang.Assert;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static be.shop.slow_delivery.exception.ErrorCode.CATEGORY_COUNT;

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
    private Long thumbnailFileId;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "min_order_amount", nullable = false))
    private Money minOrderAmount;
    @Column(name = "description", nullable = true)
    private String description;
    @Embedded
    private PhoneNumber phoneNumber;
    @Embedded
    private BusinessTimeInfo businessTimeInfo;
    @Embedded
    private ShopLocation location;
    @Column(name = "is_open", nullable = false)
    private boolean isOpen;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryShop> categories = new ArrayList<>();

    @Builder
    public Shop(String name,
                Money minOrderAmount,
                String phoneNumber,
                String description,
                String openingHours,
                String dayOff,
                ShopLocation location,
                Long thumbnailFileId,
                Category category) {
        Assert.hasText(name, "가게명은 필수입니다.");
        Assert.notNull(minOrderAmount, "최소 주문 금액은 필수입니다.");
        Assert.hasText(phoneNumber, "전화번호는 필수입니다.");
        Assert.notNull(location, "가게 위치는 필수입니다.");
        Assert.notNull(category, "카테고리는 필수입니다.");

        this.name = name;
        this.minOrderAmount = minOrderAmount;
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.description = description;
        this.businessTimeInfo = new BusinessTimeInfo(openingHours, dayOff);
        this.location = location;
        this.thumbnailFileId = thumbnailFileId;
        this.categories.add(new CategoryShop(this, category.getId()));
        if(this.categories.size() == 0)
            throw new InvalidValueException(CATEGORY_COUNT);
        this.isOpen = false;
    }

    public void updateShopThumbnail(Long fileId) {
        this.thumbnailFileId = fileId;
    }

    public void belongToCategory(Category category) {
        categories.add(new CategoryShop(this, category.getId()));
    }

    public void toggleOpen() {
        isOpen = !isOpen;
    }

    public void update(ShopModifyDomain modify) {
        if (modify.getDescription() != null)
            this.description = modify.getDescription();
        if (modify.getMinOrderAmount() != null)
            this.minOrderAmount = modify.getMinOrderAmount();
        if (modify.getBusinessTimeInfo() != null) {
            this.businessTimeInfo.update(modify.getBusinessTimeInfo());
        }
    }
}
