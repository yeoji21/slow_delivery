package be.shop.slow_delivery.stock.infra;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.exception.BusinessException;
import be.shop.slow_delivery.exception.ErrorCode;
import be.shop.slow_delivery.stock.domain.StockStore;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisStore implements StockStore {
    private final RedissonClient redissonClient;
    private static final String LOCK_SUFFIX = ":lock";
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
    public <T> void save(String key, T value) {
        redissonClient.getBucket(key).set(value);
    }

    @Override
    public void executeWithLock(String key, Runnable function) {
        RLock lock = redissonClient.getLock(key + LOCK_SUFFIX);

        try {
            boolean isLocked = lock.tryLock(WAIT_TIME_SECONDS, LEASE_TIME_SECONDS, TimeUnit.SECONDS);
            if (!isLocked) throw new BusinessException(ErrorCode.REDIS_UNAVAILABLE);
            function.run();
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void executeWithMultiLock(List<String> keys, Runnable runnable) {
        RLock[] rLocks = keys.stream()
                .map(key -> redissonClient.getLock(key + LOCK_SUFFIX))
                .toArray(RLock[]::new);
        RLock multiLock = redissonClient.getMultiLock(rLocks);

        try {
            boolean isLocked = multiLock.tryLock(WAIT_TIME_SECONDS, LEASE_TIME_SECONDS, TimeUnit.SECONDS);
            if (!isLocked) throw new BusinessException(ErrorCode.REDIS_UNAVAILABLE);
            runnable.run();
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        } finally {
            multiLock.unlock();
        }
    }

    @Override
    public long atomicIncrease(String key, Quantity quantity) {
        return redissonClient.getAtomicLong(key).addAndGet(quantity.toInt());
    }

    @Override
    public long atomicDecrease(String key, Quantity quantity) {
        return redissonClient.getAtomicLong(key).addAndGet(quantity.toInt() * -1);
    }
}
