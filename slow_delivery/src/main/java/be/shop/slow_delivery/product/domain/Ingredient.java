package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import com.mysema.commons.lang.Assert;
import lombok.*;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Ingredient extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Embedded
    private Money price;
    @Column(name = "stock_id", nullable = false)
    private Long stockId;
    @Column(name = "is_sale", nullable = false)
    private boolean isSale;

    @Builder
    public Ingredient(String name,
                      Money price,
                      Long stockId) {
        Assert.hasText(name, "name");
        Assert.notNull(price, "price");
        Assert.notNull(stockId, "stockId");

        this.name = name;
        this.price = price;
        this.stockId = stockId;
        this.isSale = true;
    }
}
