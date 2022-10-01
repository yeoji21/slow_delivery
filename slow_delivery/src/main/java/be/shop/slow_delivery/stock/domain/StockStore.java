package be.shop.slow_delivery.stock.domain;

import java.util.List;
import java.util.Optional;

public interface StockStore {
    Optional<Integer> getValue(long stockId);
    <T> T save(long stockId, T value);
    void executeWithLock(String key, Runnable function);
    void executeWithMultiLock(List<Long> stockIds, Runnable runnable);
}
