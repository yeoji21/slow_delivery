package be.shop.slow_delivery.stock.domain.event;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.stock.domain.StockRepository;
import be.shop.slow_delivery.stock.domain.StockStore;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class StockUpdatedEventListener {
    private final StockRepository stockRepository;
    private final StockStore stockStore;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void stockUpdatedEventListener(StockUpdatedEvent event) {
        event.getStockIds().stream()
                .sorted()
                .forEach(stockId -> stockRepository.setStockQuantity(stockId,
                        new Quantity(stockStore.getValue(stockId).orElseThrow(IllegalArgumentException::new)))
        );
    }
}
