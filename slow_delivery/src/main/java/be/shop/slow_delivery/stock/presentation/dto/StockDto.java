package be.shop.slow_delivery.stock.presentation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StockDto {
    private long stockId;
    private int quantity;

    public StockDto(long stockId, int quantity) {
        this.stockId = stockId;
        this.quantity = quantity;
    }
}
