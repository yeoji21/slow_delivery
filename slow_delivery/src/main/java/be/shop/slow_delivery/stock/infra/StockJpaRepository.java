package be.shop.slow_delivery.stock.infra;

import be.shop.slow_delivery.stock.domain.Stock;
import be.shop.slow_delivery.stock.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class StockJpaRepository implements StockRepository {
    private final EntityManager entityManager;

    @Override
    public void save(Stock stock) {
        entityManager.persist(stock);
    }
}
