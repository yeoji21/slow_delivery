package be.shop.slow_delivery.stock.application;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.exception.ErrorCode;
import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.product.domain.Product;
import be.shop.slow_delivery.product.domain.ProductRepository;
import be.shop.slow_delivery.stock.application.dto.StockReduceCommand;
import be.shop.slow_delivery.stock.domain.Stock;
import be.shop.slow_delivery.stock.domain.StockRepository;
import be.shop.slow_delivery.stock.domain.StockStore;
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
    private final ProductRepository productRepository;
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
    public void reduceByRedisson(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);

        stockStore.executeWithLock(product.getStockKey(), () -> {
            Integer stock = stockStore.getValue(product.getStockKey())
                    .orElseThrow(IllegalArgumentException::new);
            System.out.println(stock);
            if(stock <= 0){
                log.info("재고 수량 : " + stock + " , 주문 수량 : " + 1);
                throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다.");
            }
            stockStore.save(product.getStockKey(), stock - 1);
            return null;
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
