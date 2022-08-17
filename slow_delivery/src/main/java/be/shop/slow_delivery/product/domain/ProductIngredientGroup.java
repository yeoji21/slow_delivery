package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.DisplayOrder;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductIngredientGroup extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_group_id")
    private IngredientGroup ingredientGroup;

    @Embedded
    private DisplayOrder displayOrder;

    @Builder
    public ProductIngredientGroup(Product product,
                                  IngredientGroup ingredientGroup,
                                  Integer displayOrder) {
        Assert.notNull(product, "product");
        Assert.notNull(product, "ingredientGroup");
        Assert.notNull(displayOrder, "displayOrder");

        this.product = product;
        this.ingredientGroup = ingredientGroup;
        this.displayOrder = new DisplayOrder(displayOrder);
    }
}
