package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductIngredientGroup extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_ingredient_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_group_id")
    private IngredientGroup ingredientGroup;

    @Builder
    public ProductIngredientGroup(Product product, IngredientGroup ingredientGroup) {
        Assert.notNull(product, "product");
        Assert.notNull(product, "ingredientGroup");

        this.product = product;
        this.ingredientGroup = ingredientGroup;
    }
}
