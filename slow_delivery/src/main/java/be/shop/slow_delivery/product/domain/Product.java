package be.shop.slow_delivery.product.domain;


import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import lombok.*;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "thumbnail_file_id", nullable = true)
    private Long thumbnailFileId;

    @Embedded
    private StockInfo stockInfo;

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

    @Builder
    public Product(Long stockId,
                   Long thumbnailFileId,
                   String name,
                   String description,
                   Money price,
                   Quantity maxOrderQuantity) {
        this.thumbnailFileId = thumbnailFileId;
        this.stockInfo = new StockInfo(stockId);
        this.name = name;
        this.description = description;
        this.price = price;
        this.maxOrderQuantity = maxOrderQuantity;
    }
}
