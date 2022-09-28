package be.shop.slow_delivery.stock.application.dto;


import be.shop.slow_delivery.common.domain.Quantity;
import com.mysema.commons.lang.Assert;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockReduceCommand {
    private long stockId;
    private Quantity quantity;

    public StockReduceCommand(long stockId, Quantity quantity) {
        Assert.isTrue(quantity.toInt() > 0, "invalid quantity");
        this.stockId = stockId;
        this.quantity = quantity;
    }
}
