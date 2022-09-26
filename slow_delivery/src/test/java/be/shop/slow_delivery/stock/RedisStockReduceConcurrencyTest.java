package be.shop.slow_delivery.stock;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.domain.Product;
import be.shop.slow_delivery.stock.application.StockCommandService;
import be.shop.slow_delivery.stock.domain.Stock;
import be.shop.slow_delivery.stock.domain.StockStore;
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

    private static long stockId, productId;

    @Test
    @Transactional
    @Rollback(value = false) @Order(1)
    void 데이터_세팅() {
        Stock stock = new Stock(new Quantity(COUNT));
        entityManager.persist(stock);
        stockId = stock.getId();

        Product product = Product.builder()
                .stockId(stock.getId())
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();
        entityManager.persist(product);
        productId = product.getId();

        stockStore.save(product.getStockKey(), stock.getQuantity().toInt());

        entityManager.flush();
        entityManager.clear();
    }

    @Test @Transactional @Order(2)
    void 재고_감소_테스트() throws Exception{
        //given
        Product product = entityManager.find(Product.class, productId);
        Integer before = stockStore.getValue(product.getStockKey()).orElseThrow(IllegalArgumentException::new);
        System.out.println("before stock : " + before);

        CountDownLatch latch = new CountDownLatch(COUNT);

        //when
        for (int i = 0; i < COUNT; i++) {
            executorService.execute(() -> {
                stockCommandService.reduceByRedisson(productId);
                latch.countDown();
            });
        }

        //then
        latch.await();
        entityManager.flush();
        entityManager.clear();

        Integer after = stockStore.getValue(product.getStockKey()).orElseThrow(IllegalArgumentException::new);
        System.out.println("after stock : " + after);

        assertThat(after).isEqualTo(0);
        assertThat(after + before).isEqualTo(COUNT);
    }
}
