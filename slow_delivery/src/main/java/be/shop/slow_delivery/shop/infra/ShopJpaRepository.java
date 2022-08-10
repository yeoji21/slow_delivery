package be.shop.slow_delivery.shop.infra;

import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopRepository;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShopJpaRepository extends JpaRepository<Shop, Long>, ShopRepository {
}
