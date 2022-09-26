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
    public void reduceByRedisson(List<StockReduceCommand> commands) {
        List<String> keys = commands.stream()
                .map(command -> RedisKeyResolver.getKey(command.getStockId()))
                .collect(Collectors.toList());

        stockStore.executeWithMultiLock(keys, () -> {
            List<Integer> nowStocks = new ArrayList<>();
            commands.forEach(command -> {
                int remainingStock = stockStore.getValue(RedisKeyResolver.getKey(command.getStockId()))
                        .orElseThrow(IllegalArgumentException::new);
                int nowStock = remainingStock - command.getQuantity().toInt();
                if(nowStock < 0){
                    log.info("재고 ID "+ command.getStockId() + " 재고 수량 : " + remainingStock + " , 주문 수량 : " + command.getQuantity().toInt());
                    throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다.");
                }
                nowStocks.add(nowStock);
            });

            IntStream.range(0, keys.size())
                    .forEach(i -> stockStore.save(keys.get(i), nowStocks.get(i)));
        });
    }

    @Transactional
    public void reduce(List<StockReduceCommand> commands) {
        // 데드락 방지
        commands.sort(Comparator.comparing(StockReduceCommand::getStockId));

        for (int i = 0; i < commands.size(); i++) {
            Stock stock = stockRepository.findByIdForUpdate(commands.get(i).getStockId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.STOCK_NOT_FOUND));
            stock.reduceStock(commands.get(i).getQuantity());
        }
    }
}
