package be.shop.slow_delivery.stock.domain;

import java.util.Optional;

public interface StockStore {
    Optional<Integer> getValue(String key);
    void save(String key, String value);

    <T> void save(String key, T value);

    void executeWithLock(String key, Runnable function);
}
