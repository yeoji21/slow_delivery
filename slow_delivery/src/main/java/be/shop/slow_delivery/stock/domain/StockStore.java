package be.shop.slow_delivery.stock.domain;

import be.shop.slow_delivery.common.domain.Quantity;

import java.util.List;
import java.util.Optional;

public interface StockStore {
    Optional<Integer> getValue(long stockId);
    <T> T save(long stockId, T value);
    void executeWithMultiLock(List<Long> stockIds, Runnable runnable);
    void reduce(long stockId, Quantity quantity);
}
