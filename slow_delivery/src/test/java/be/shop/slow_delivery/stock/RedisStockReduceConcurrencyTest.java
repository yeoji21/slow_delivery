package be.shop.slow_delivery.stock;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.stock.application.StockCommandService;
import be.shop.slow_delivery.stock.application.dto.StockReduceCommand;
import be.shop.slow_delivery.stock.domain.Stock;
import be.shop.slow_delivery.stock.domain.StockStore;
import be.shop.slow_delivery.stock.infra.RedisKeyResolver;
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
public class RedisStockReduceConcurrencyTest {
    private final int COUNT = 100;
    private final ExecutorService executorService = Executors.newFixedThreadPool(COUNT);

    @Autowired private StockCommandService stockCommandService;
    @Autowired private EntityManager entityManager;
    @Autowired private StockStore stockStore;

    private static long stockId;

    @Test
    @Transactional
    @Rollback(value = false) @Order(1)
    void 데이터_세팅() {
        Stock stock = new Stock(new Quantity(COUNT));
        entityManager.persist(stock);
        stockId = stock.getId();

        stockStore.save(RedisKeyResolver.getKey(stockId), stock.getQuantity().toInt());

        entityManager.flush();
        entityManager.clear();
    }

    @Test @Transactional @Order(2)
    void 재고_감소_테스트() throws Exception{
        //given
        String key = RedisKeyResolver.getKey(stockId);
        Integer before = stockStore.getValue(key).orElseThrow(IllegalArgumentException::new);
        System.out.println("before stock : " + before);

        CountDownLatch latch = new CountDownLatch(COUNT);

        //when
        for (int i = 0; i < COUNT; i++) {
            executorService.execute(() -> {
                stockCommandService.reduceByRedisson(new StockReduceCommand(stockId, new Quantity(1)));
                latch.countDown();
            });
        }

        //then
        latch.await();
        entityManager.flush();
        entityManager.clear();

        Integer after = stockStore.getValue(key).orElseThrow(IllegalArgumentException::new);
        System.out.println("after stock : " + after);

        assertThat(after).isEqualTo(0);
        assertThat(after + before).isEqualTo(COUNT);
    }
}
