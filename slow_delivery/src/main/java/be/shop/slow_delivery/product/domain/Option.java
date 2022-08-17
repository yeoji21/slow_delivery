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
public class Option extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    @Column(name = "price", nullable = false)
    private Money price;

    @Embedded
    private StockInfo stockInfo;

    @Builder
    public Option(String name,
                  Money price,
                  Long stockId) {
        Assert.hasText(name, "name");
        Assert.notNull(price, "price");
        Assert.notNull(stockId, "stockId");

        this.name = name;
        this.price = price;
        this.stockInfo = new StockInfo(stockId);
    }
}
