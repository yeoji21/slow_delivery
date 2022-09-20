package be.shop.slow_delivery.stock.presentation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StockReduceDto {
    private List<StockDto> stocks;

    public StockReduceDto(List<StockDto> stocks) {
        this.stocks = stocks;
    }
}
