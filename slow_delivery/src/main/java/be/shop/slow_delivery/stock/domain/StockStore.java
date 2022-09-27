package be.shop.slow_delivery.stock.domain;

import be.shop.slow_delivery.common.domain.Quantity;

import java.util.List;
import java.util.Optional;

public interface StockStore {
    Optional<Integer> getValue(String key);

    <T> void save(String key, T value);

    void executeWithLock(String key, Runnable function);

    void executeWithMultiLock(List<String> keys, Runnable runnable);

    long atomicDecrease(String key, Quantity quantity);
    long atomicIncrease(String key, Quantity quantity);
}
