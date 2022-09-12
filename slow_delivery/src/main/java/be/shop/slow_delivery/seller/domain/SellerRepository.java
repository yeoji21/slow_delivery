package be.shop.slow_delivery.seller.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Integer> {

    Seller save(Seller seller);

    Optional<Seller> findByEmail(String email);

    Seller findById(Long sellerId);

    Optional<Seller> findByLoginId(String loginId);

}
