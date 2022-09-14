package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import com.mysema.commons.lang.Assert;
import lombok.*;

import javax.persistence.*;

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

    @Builder
    public IngredientGroup(String name, SelectCount selectCount) {
        Assert.hasText(name, "name");
        Assert.notNull(selectCount, "selectCount");

        this.name = name;
        this.selectCount = selectCount;
    }
}
