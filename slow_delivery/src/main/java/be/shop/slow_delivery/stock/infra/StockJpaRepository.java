package be.shop.slow_delivery.stock.infra;

import be.shop.slow_delivery.stock.domain.Stock;
import be.shop.slow_delivery.stock.domain.StockRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.Optional;

import static be.shop.slow_delivery.stock.domain.QStock.stock;

@RequiredArgsConstructor
@Repository
public class StockJpaRepository implements StockRepository {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public void save(Stock stock) {
        entityManager.persist(stock);
    }

    @Override
    public Optional<Stock> findByIdForUpdate(long stockId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(stock)
                        .where(stock.id.eq(stockId))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        );
    }

    @Override
    public Optional<Stock> findById(long stockId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(stock)
                        .where(stock.id.eq(stockId))
                        .fetchOne()
        );
    }
}
