package be.shop.slow_delivery.product.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class StockInfo {
    @Column(name = "stock_id", nullable = false)
    private Long stockId;
    @Column(name = "is_sale", nullable = false)
    private boolean isSale;

    public StockInfo(Long stockId) {
        this.stockId = stockId;
        this.isSale = true;
    }
}
