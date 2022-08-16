package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IngredientGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_group_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private SelectCount selectCount;

    @Builder
    public IngredientGroup(String name, SelectCount selectCount) {
        Assert.hasText(name, "이름은 필수입니다.");
        Assert.notNull(selectCount, "선택 개수는 필수입니다.");

        this.name = name;
        this.selectCount = selectCount;
    }
}
