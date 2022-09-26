package be.shop.slow_delivery.stock.domain;

import java.util.Optional;
import java.util.function.Supplier;

public interface StockStore {
    Optional<Integer> getValue(String key);
    void save(String key, String value);

    <T> void save(String key, T value);

    <T> T executeWithLock(String key, Supplier<T> function);
}
