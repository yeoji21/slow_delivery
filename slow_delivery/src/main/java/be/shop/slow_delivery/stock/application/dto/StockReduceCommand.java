package be.shop.slow_delivery.stock.application.dto;


import be.shop.slow_delivery.common.domain.Quantity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StockReduceCommand {
    private long stockId;
    private Quantity quantity;
}
