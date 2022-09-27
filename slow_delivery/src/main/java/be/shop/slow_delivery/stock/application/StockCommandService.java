package be.shop.slow_delivery.stock.application;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.exception.ErrorCode;
import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.stock.application.dto.StockReduceCommand;
import be.shop.slow_delivery.stock.domain.Stock;
import be.shop.slow_delivery.stock.domain.StockRepository;
import be.shop.slow_delivery.stock.domain.StockStore;
import be.shop.slow_delivery.stock.infra.RedisKeyResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockCommandService {
    private final StockRepository stockRepository;
    private final StockStore stockStore;

    @Transactional
    public long create(Quantity quantity) {
        Stock stock = new Stock(quantity);
        stockRepository.save(stock);
        return stock.getId();
    }

    @Transactional
    public void add(long stockId, Quantity quantity) {
        Stock stock = stockRepository.findByIdForUpdate(stockId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STOCK_NOT_FOUND));
        stock.addStock(quantity);
    }

    @Transactional
    public void reduceByRedissonLock(List<StockReduceCommand> commands) {
        List<String> keys = commands.stream()
                .map(command -> RedisKeyResolver.getKey(command.getStockId()))
                .collect(Collectors.toList());

        stockStore.executeWithMultiLock(keys, () -> {
            List<Integer> stocks = new ArrayList<>();
            commands.forEach(command -> {
                int remainingStock = stockStore.getValue(RedisKeyResolver.getKey(command.getStockId()))
                                                .orElseThrow(IllegalArgumentException::new);
                stocks.add(getReducedStock(command, remainingStock));
            });

            IntStream.range(0, keys.size())
                    .forEach(i -> stockStore.save(keys.get(i), stocks.get(i)));
        });
    }

    @Transactional
    public void reduceByAtomic(List<StockReduceCommand> commands) {
        for (int commandIdx = 0; commandIdx < commands.size(); commandIdx++) {
            StockReduceCommand command = commands.get(commandIdx);
            long nowStock = stockStore.atomicDecrease(RedisKeyResolver.getKey(command.getStockId()), command.getQuantity());
            if (nowStock < 0) {
                for (int rewardIdx = 0; rewardIdx <= commandIdx; rewardIdx++) {
                    StockReduceCommand rewardCommand = commands.get(rewardIdx);
                    stockStore.atomicIncrease(RedisKeyResolver.getKey(rewardCommand.getStockId()), rewardCommand.getQuantity());
                }
                throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다.");
            }
        }
    }

    @Transactional
    public void reduceByDBLock(List<StockReduceCommand> commands) {
        // 데드락 방지
        commands.sort(Comparator.comparing(StockReduceCommand::getStockId));

        for (int i = 0; i < commands.size(); i++) {
            Stock stock = stockRepository.findByIdForUpdate(commands.get(i).getStockId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.STOCK_NOT_FOUND));
            stock.reduceStock(commands.get(i).getQuantity());
        }
    }

    private int getReducedStock(StockReduceCommand command, int remainingStock) {
        int nowStock = remainingStock - command.getQuantity().toInt();
        if(nowStock < 0) throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다.");
        return nowStock;
    }
}
