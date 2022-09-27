package be.shop.slow_delivery.stock.application;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.stock.application.dto.StockReduceCommand;
import be.shop.slow_delivery.stock.domain.Stock;
import be.shop.slow_delivery.stock.domain.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RedissonClient;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class StockCommandServiceTest {
    @Mock private StockRepository stockRepository;
    @Mock private RedissonClient redissonClient;
    @InjectMocks private StockCommandService stockCommandService;

    @Test @DisplayName("새로운 재고 저장")
    void create() throws Exception{
        //given
        Quantity quantity = new Quantity(50);
        doAnswer((invocation) -> {
            Stock argument = (Stock) invocation.getArgument(0);
            ReflectionTestUtils.setField(argument, "id", 1L);
            return argument;
        }).when(stockRepository).save(any(Stock.class));

        //when
        long stockId = stockCommandService.create(quantity);

        //then
        assertThat(stockId).isGreaterThan(0L);
    }

    @Test @DisplayName("재고량 추가")
    void add() throws Exception{
        //given
        Stock stock = new Stock(new Quantity(10));
        ReflectionTestUtils.setField(stock, "id", 1L);
        given(stockRepository.findByIdForUpdate(any(Long.class))).willReturn(Optional.of(stock));

        //when
        stockCommandService.add(stock.getId(), new Quantity(50));

        //then
        assertThat(stock.getQuantity().toInt()).isEqualTo(60);
    }

    @Test @DisplayName("재고량 감소")
    void reduce() throws Exception{
        //given
        Stock stock1 = new Stock(new Quantity(10));
        ReflectionTestUtils.setField(stock1, "id", 1L);
        Stock stock2 = new Stock(new Quantity(10));
        ReflectionTestUtils.setField(stock2, "id", 2L);
        Stock stock3 = new Stock(new Quantity(10));
        ReflectionTestUtils.setField(stock3, "id", 3L);

        List<StockReduceCommand> commands = new ArrayList<>();
        commands.add(new StockReduceCommand(stock1.getId(), new Quantity(3)));
        commands.add(new StockReduceCommand(stock2.getId(), new Quantity(1)));
        commands.add(new StockReduceCommand(stock3.getId(), new Quantity(1)));

        given(stockRepository.findByIdForUpdate(stock1.getId())).willReturn(Optional.of(stock1));
        given(stockRepository.findByIdForUpdate(stock2.getId())).willReturn(Optional.of(stock2));
        given(stockRepository.findByIdForUpdate(stock3.getId())).willReturn(Optional.of(stock3));

        //when
        stockCommandService.reduceByDBLock(commands);

        //then
        assertThat(stock1.getQuantity()).isEqualTo(new Quantity(7));
        assertThat(stock2.getQuantity()).isEqualTo(new Quantity(9));
        assertThat(stock3.getQuantity()).isEqualTo(new Quantity(9));
    }
}