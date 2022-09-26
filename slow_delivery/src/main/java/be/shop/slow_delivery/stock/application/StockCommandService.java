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

import java.util.Comparator;
import java.util.List;

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
    public void reduceByRedisson(StockReduceCommand command) {
        String key = RedisKeyResolver.getKey(command.getStockId());

        stockStore.executeWithLock(key, () -> {
            int remainingStock = stockStore.getValue(key)
                    .orElseThrow(IllegalArgumentException::new);
            if(remainingStock <= 0){
                log.info("재고 수량 : " + remainingStock + " , 주문 수량 : " + command.getQuantity().toInt());
                throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다.");
            }
            stockStore.save(key, new Quantity(remainingStock).minus(command.getQuantity()).toInt());
        });
    }

    @Transactional
    public void reduce(List<StockReduceCommand> reduceCommands) {
        // 데드락 방지
        reduceCommands.sort(Comparator.comparing(StockReduceCommand::getStockId));

        for (int i = 0; i < reduceCommands.size(); i++) {
            Stock stock = stockRepository.findByIdForUpdate(reduceCommands.get(i).getStockId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.STOCK_NOT_FOUND));
            stock.reduceStock(reduceCommands.get(i).getQuantity());
        }
    }
}
