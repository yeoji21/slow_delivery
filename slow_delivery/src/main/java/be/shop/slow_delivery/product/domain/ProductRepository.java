package be.shop.slow_delivery.product.domain;

import java.util.Optional;

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(long productId);
}
