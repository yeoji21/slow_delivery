package be.shop.slow_delivery.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
    Optional<Menu> findById(Long menuId);
    List<Menu> findAllByShopId(Long shopId);
}