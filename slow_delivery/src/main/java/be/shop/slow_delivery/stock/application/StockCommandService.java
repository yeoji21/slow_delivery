package be.shop.slow_delivery.stock.application;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.exception.ErrorCode;
import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.stock.application.dto.StockReduceCommand;
import be.shop.slow_delivery.stock.domain.Stock;
import be.shop.slow_delivery.stock.domain.StockRepository;
import be.shop.slow_delivery.stock.domain.StockStore;
import be.shop.slow_delivery.stock.domain.event.StockUpdatedEvent;
import com.mysema.commons.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockCommandService {
    private final ApplicationEventPublisher eventPublisher;
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
        List<Long> stockIds = commands.stream()
                .map(StockReduceCommand::getStockId)
                .collect(Collectors.toList());

        stockStore.executeWithMultiLock(stockIds, () -> {
            List<Integer> reducedStocks = commands.stream()
                    .map(command -> {
                        int remainingStock = stockStore.getValue(command.getStockId())
                                .orElseGet(() -> stockStore.save(command.getStockId(), stockRepository.findById(command.getStockId())
                                        .orElseThrow(IllegalArgumentException::new).getQuantity().toInt())
                        );
                        return getReducedStock(command, remainingStock);
                    })
                    .collect(Collectors.toList());
            IntStream.range(0, reducedStocks.size())
                    .forEach(idx -> stockStore.save(commands.get(idx).getStockId(), reducedStocks.get(idx)));
        });
        eventPublisher.publishEvent(new StockUpdatedEvent(stockIds));
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
        int reducedStock = remainingStock - command.getQuantity().toInt();
        Assert.isTrue(reducedStock >= 0, "주문 수량이 재고 수량보다 많습니다.");
        return reducedStock;
    }
}
