package be.shop.slow_delivery.stock.domain;

import java.util.List;
import java.util.Optional;

public interface StockStore {
    Optional<Integer> getValue(String key);

    <T> void save(String key, T value);

    void executeWithLock(String key, Runnable function);

    void executeWithMultiLock(List<String> keys, Runnable runnable);
}
