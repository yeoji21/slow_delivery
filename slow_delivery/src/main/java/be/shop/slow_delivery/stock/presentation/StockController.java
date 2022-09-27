package be.shop.slow_delivery.stock.presentation;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.stock.application.StockCommandService;
import be.shop.slow_delivery.stock.application.dto.StockReduceCommand;
import be.shop.slow_delivery.stock.presentation.dto.StockReduceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class StockController {
    private final StockCommandService stockCommandService;

    @PostMapping("/test")
    public void reduceByRedis(@RequestBody StockReduceDto stockReduceDto) {
        List<StockReduceCommand> commands = stockReduceDto.getStocks().stream()
                .map(dto -> StockReduceCommand.builder()
                        .stockId(dto.getStockId())
                        .quantity(new Quantity(dto.getQuantity()))
                        .build())
                .collect(Collectors.toList());
        stockCommandService.reduceByRedissonLock(commands);
//        stockCommandService.reduceByDBLock(commands);
    }

    @PatchMapping("/stock/reduce")
    public void reduceStock(@RequestBody StockReduceDto stockReduceDto) {
        List<StockReduceCommand> commands = stockReduceDto.getStocks().stream()
                .map(dto -> StockReduceCommand.builder()
                        .stockId(dto.getStockId())
                        .quantity(new Quantity(dto.getQuantity()))
                        .build())
                .collect(Collectors.toList());
        stockCommandService.reduceByDBLock(commands);
    }
}
