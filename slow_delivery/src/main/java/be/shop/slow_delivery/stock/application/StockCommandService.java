package be.shop.slow_delivery.stock.application;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.exception.ErrorCode;
import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.stock.domain.Stock;
import be.shop.slow_delivery.stock.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockCommandService {
    private final StockRepository stockRepository;

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
}
