package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Ingredient extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Embedded
    private Money price;
    @Embedded
    private StockInfo stockInfo;

    @Builder
    public Ingredient(String name,
                      Money price,
                      Long stockId) {
        Assert.hasText(name, "이름은 필수입니다.");
        Assert.notNull(price, "가격은 필수입니다.");
        Assert.notNull(stockId, "재고는 필수입니다.");

        this.name = name;
        this.price = price;
        this.stockInfo = new StockInfo(stockId);
    }
}
