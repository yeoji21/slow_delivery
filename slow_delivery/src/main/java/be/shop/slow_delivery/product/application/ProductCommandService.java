package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.product.application.command.ProductCreateCommand;
import be.shop.slow_delivery.product.domain.Product;
import be.shop.slow_delivery.product.domain.ProductRepository;
import be.shop.slow_delivery.stock.application.StockCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final StockCommandService stockCommandService;

    @Transactional
    public long create(ProductCreateCommand command) {
        long stockId = stockCommandService.create(command.getStock());
        Product product = Product.builder()
                .name(command.getName())
                .description(command.getDescription())
                .price(command.getPrice())
                .maxOrderQuantity(command.getMaxOrderQuantity())
                .stockId(stockId)
                .build();
        productRepository.save(product);
        return product.getId();
    }
}






















