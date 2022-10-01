package be.shop.slow_delivery.stock.domain;

import be.shop.slow_delivery.common.domain.Quantity;

import java.util.Optional;

public interface StockRepository {
    void save(Stock stock);
    Optional<Stock> findByIdForUpdate(long stockId);
    Optional<Stock> findById(long stockId);
    void setStockQuantity(long stockId, Quantity quantity);
}
