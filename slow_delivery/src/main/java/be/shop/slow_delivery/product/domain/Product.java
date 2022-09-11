package be.shop.slow_delivery.product.domain;


import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.domain.validate.ProductValidate;
import com.mysema.commons.lang.Assert;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "thumbnail_file_id", nullable = true)
    private Long thumbnailFileId;

    @Column(name = "stock_id", nullable = false)
    private Long stockId;

    @Column(name = "is_sale", nullable = false)
    private boolean isSale;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Embedded
    @Column(name = "price", nullable = false)
    private Money price;

    @Embedded
    @Column(name = "max_order_quantity", nullable = false)
    private Quantity maxOrderQuantity;

    @OrderBy("displayOrder")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductIngredientGroup> ingredientGroups = new ArrayList<>();

    @Builder
    public Product(Long stockId,
                   Long thumbnailFileId,
                   String name,
                   String description,
                   Money price,
                   Quantity maxOrderQuantity) {
        Assert.notNull(stockId, "stockId");
        Assert.hasText(name, "name");
        Assert.notNull(price, "price");
        Assert.notNull(maxOrderQuantity, "maxOrderQuantity");

        this.thumbnailFileId = thumbnailFileId;
        this.stockId = stockId;
        this.isSale = true;
        this.name = name;
        this.description = description;
        this.price = price;
        this.maxOrderQuantity = maxOrderQuantity;
    }

    public void addIngredientGroup(IngredientGroup ingredientGroup, int displayOrder) {
        ingredientGroups.add(new ProductIngredientGroup(this, ingredientGroup, displayOrder));
    }

    public void validate(ProductValidate validate) {
        Assert.isTrue(isSale, "isSale");
        Assert.isTrue(name.equals(validate.getName()), "productName");
        Assert.isTrue(price.equals(validate.getPrice()), "productPrice");
        Assert.isTrue(maxOrderQuantity.minus(validate.getOrderQuantity()).toInt() > 0, "orderQuantity");
    }
}
