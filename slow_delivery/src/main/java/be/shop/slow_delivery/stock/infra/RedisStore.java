package be.shop.slow_delivery.stock.infra;

import be.shop.slow_delivery.stock.domain.StockStore;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Component
public class RedisStore implements StockStore {
    private final RedissonClient redissonClient;
    private static final long WAIT_TIME_SECONDS = 3;
    private static final long LEASE_TIME_SECONDS = 3;

    @Override
    public Optional<Integer> getValue(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (bucket.isExists()) {
            return Optional.of(Integer.parseInt((String) bucket.get()));
        }
        return Optional.empty();
    }

    @Override
    public void save(String key, String value) {
        redissonClient.getBucket(key).set(value);
    }

    @Override
    public <T> void save(String key, T value) {
        redissonClient.getBucket(key).set(value);
    }

    @Override
    public <T> T executeWithLock(String key, Supplier<T> function) {
        RLock lock = redissonClient.getLock("stock:" + key);

        try {
            boolean isLocked = lock.tryLock(WAIT_TIME_SECONDS, LEASE_TIME_SECONDS, TimeUnit.SECONDS);
            if (!isLocked) {
                System.out.println("UNLOCKED");
            }
            return function.get();
        } catch (InterruptedException e) {
            System.out.println("THREAD INTERRUPTED");
        } finally {
            lock.unlock();
        }
        return null;
    }
}
