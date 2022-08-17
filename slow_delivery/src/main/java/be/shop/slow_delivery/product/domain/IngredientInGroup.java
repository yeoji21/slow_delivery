package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.DisplayInfo;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IngredientInGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_group_id")
    private IngredientGroup ingredientGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Embedded
    private DisplayInfo displayInfo;

    public IngredientInGroup(IngredientGroup ingredientGroup,
                             Ingredient ingredient,
                             int displayOrder) {
        Assert.notNull(ingredientGroup, "ingredientGroup");
        Assert.notNull(ingredient, "상품 구성");

        this.ingredientGroup = ingredientGroup;
        this.ingredient = ingredient;
        this.displayInfo = new DisplayInfo(displayOrder);
    }
}
