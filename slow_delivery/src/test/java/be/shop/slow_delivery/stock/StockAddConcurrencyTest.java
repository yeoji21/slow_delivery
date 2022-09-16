package be.shop.slow_delivery.stock;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.stock.application.StockCommandService;
import be.shop.slow_delivery.stock.domain.Stock;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@SpringBootTest
public class StockAddConcurrencyTest {
    private final int COUNT = 100;
    private final ExecutorService executorService = Executors.newFixedThreadPool(COUNT);

    @Autowired
    private StockCommandService stockCommandService;
    @Autowired
    private EntityManager entityManager;

    @Test @Transactional @Rollback(value = false) @Order(1)
    void 데이터_세팅() {
        Stock stock = new Stock(Quantity.ZERO);
        entityManager.persist(stock);

        entityManager.createQuery("update Stock s set s.id = 1L")
                .executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    @Test @Transactional @Order(2)
    void 재고_증가_테스트() throws Exception{
        //given
        Stock stock = entityManager.find(Stock.class, 1L);
        Quantity before = stock.getQuantity();

        CountDownLatch latch = new CountDownLatch(COUNT);

        //when
        for (int i = 0; i < COUNT; i++) {
            executorService.execute(() -> {
                stockCommandService.add(stock.getId(), new Quantity(1));
                latch.countDown();
            });
        }

        //then
        latch.await();
        entityManager.flush();
        entityManager.clear();

        Quantity after = entityManager.find(Stock.class, stock.getId()).getQuantity();
        assertThat(after.plus(before).toInt()).isEqualTo(COUNT);
    }
}
