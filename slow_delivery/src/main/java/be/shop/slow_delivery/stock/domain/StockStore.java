package be.shop.slow_delivery.stock.domain;

import be.shop.slow_delivery.common.domain.Quantity;

import java.util.List;
import java.util.Optional;

public interface StockStore {
    Optional<Integer> getValue(long stockId);

    <T> void save(long stockId, T value);

    void executeWithLock(String key, Runnable function);

    void executeWithMultiLock(List<Long> stockIds, Runnable runnable);

    long atomicDecrease(long stockId, Quantity quantity);
    long atomicIncrease(long stockId, Quantity quantity);
}
