package be.shop.slow_delivery.stock;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.stock.application.StockCommandService;
import be.shop.slow_delivery.stock.application.dto.StockReduceCommand;
import be.shop.slow_delivery.stock.domain.Stock;
import be.shop.slow_delivery.stock.domain.StockRepository;
import be.shop.slow_delivery.stock.domain.StockStore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
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
    @Autowired private StockRepository stockRepository;
    @Autowired private EntityManager entityManager;
    @Autowired private StockStore stockStore;
    @Autowired private RedissonClient redissonClient;

    private static long firstStockId, secondStockId, thirdStockId;

    @Test
    @Transactional
    @Rollback(value = false) @Order(1)
    void 데이터_세팅() {
        Stock firstStock = new Stock(new Quantity(COUNT));
        entityManager.persist(firstStock);
        firstStockId = firstStock.getId();

        Stock secondStock = new Stock(new Quantity(COUNT));
        entityManager.persist(secondStock);
        secondStockId = secondStock.getId();

        Stock thirdStock = new Stock(new Quantity(COUNT));
        entityManager.persist(thirdStock);
        thirdStockId = thirdStock.getId();

        redissonClient.getKeys().flushall();

        entityManager.flush();
        entityManager.clear();
    }

    @Test @Transactional @Order(2)
    void 재고_감소_테스트() throws Exception{
        //given
        CountDownLatch latch = new CountDownLatch(COUNT);
        List<StockReduceCommand> commands = List.of(
                new StockReduceCommand(firstStockId, new Quantity(1)),
                new StockReduceCommand(secondStockId, new Quantity(1)),
                new StockReduceCommand(thirdStockId, new Quantity(1))
        );

        //when
        for (int i = 0; i < COUNT; i++) {
            executorService.execute(() -> {
                stockCommandService.reduceByRedissonLock(commands);
                latch.countDown();
            });
        }

        //then
        latch.await();
        entityManager.flush();
        entityManager.clear();

        assertThat((int) stockStore.getValue(firstStockId)
                .orElseThrow(IllegalArgumentException::new)).isEqualTo(0);
        assertThat((int) stockStore.getValue(secondStockId)
                .orElseThrow(IllegalArgumentException::new)).isEqualTo(0);
        assertThat((int) stockStore.getValue(thirdStockId)
                .orElseThrow(IllegalArgumentException::new)).isEqualTo(0);

        Thread.sleep(3000);

        assertThat(stockRepository.findById(firstStockId)
                .orElseThrow(IllegalArgumentException::new).getQuantity()).isEqualTo(Quantity.ZERO);
        assertThat(stockRepository.findById(secondStockId)
                .orElseThrow(IllegalArgumentException::new).getQuantity()).isEqualTo(Quantity.ZERO);
        assertThat(stockRepository.findById(thirdStockId)
                .orElseThrow(IllegalArgumentException::new).getQuantity()).isEqualTo(Quantity.ZERO);
    }
}
