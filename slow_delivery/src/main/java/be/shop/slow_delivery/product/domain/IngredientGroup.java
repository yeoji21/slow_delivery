package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import com.mysema.commons.lang.Assert;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IngredientGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private SelectCount selectCount;

    @OrderBy("displayInfo.displayOrder")
    @OneToMany(mappedBy = "ingredientGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientInGroup> ingredients = new ArrayList<>();

    @Builder
    public IngredientGroup(String name, SelectCount selectCount) {
        Assert.hasText(name, "name");
        Assert.notNull(selectCount, "selectCount");

        this.name = name;
        this.selectCount = selectCount;
    }

    public void validate(List<Ingredient> ingredients) {
        selectCount.selectedCountCheck(ingredients.size());

    }


    public void addIngredient(Ingredient ingredient, int displayOrder) {
        ingredients.add(new IngredientInGroup(this, ingredient, displayOrder));
    }
}
