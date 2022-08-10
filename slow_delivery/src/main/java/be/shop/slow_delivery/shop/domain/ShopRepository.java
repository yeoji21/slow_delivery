package be.shop.slow_delivery.shop.domain;


import java.util.Optional;

public interface ShopRepository {
    Shop save(Shop shop);
    Optional<Shop> findById(Long ShopId);
}
