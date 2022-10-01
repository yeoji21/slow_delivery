package be.shop.slow_delivery.stock.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class StockUpdatedEvent {
    private final List<Long> stockIds;
}
