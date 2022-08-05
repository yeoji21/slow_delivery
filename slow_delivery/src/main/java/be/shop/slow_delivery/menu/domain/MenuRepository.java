package be.shop.slow_delivery.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu,Long> {
//    Menu findByMenuPK(Long menuPK);
}