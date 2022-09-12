package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.command.ProductCreateCommand;
import be.shop.slow_delivery.product.domain.Product;
import be.shop.slow_delivery.product.domain.ProductRepository;
import be.shop.slow_delivery.stock.application.StockCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class ProductCommandServiceTest {
    @Mock private ProductRepository productRepository;
    @Mock private StockCommandService stockCommandService;
    @InjectMocks private ProductCommandService productCommandService;

    @Test
    void 상품_생성() throws Exception{
        //given
        ProductCreateCommand command = ProductCreateCommand.builder()
                .name("product")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(3))
                .stock(new Quantity(300))
                .build();
        given(stockCommandService.create(any(Quantity.class))).willReturn(1L);
        doAnswer((invocation) -> {
            Product argument = (Product) invocation.getArgument(0);
            ReflectionTestUtils.setField(argument, "id", 1L);
            return argument;
        }).when(productRepository).save(any(Product.class));

        //when
        long productId = productCommandService.create(command);

        //then
        assertThat(productId).isEqualTo(1L);
    }
}