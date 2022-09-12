package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.command.*;
import be.shop.slow_delivery.product.domain.validate.IngredientGroupValidate;
import be.shop.slow_delivery.product.infra.ProductQueryDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceTest {
    @Mock private ProductQueryDao productQueryDao;
    @InjectMocks private ProductQueryService productQueryService;


    @Test
    void 상품_검증_테스트() throws Exception{
        //given
        ProductValidateCommand command = ProductValidateCommand.builder()
                .id(1L)
                .name("productA")
                .price(new Money(10_000))
                .orderQuantity(new Quantity(1))
                .ingredientGroups(getIngredientGroupValidateCommands())
                .optionGroups(List.of(getOptionGroupValidateCommand()))
                .build();

        List<IngredientGroupValidate> validates = command.toIngredientGroupValidate();
        ReflectionTestUtils.setField(validates.get(0).getSelectCount().getMaxCount(), "quantity", 2);

        given(productQueryDao.findProductValidate(any(Long.class))).willReturn(Optional.ofNullable(command.toProductValidate()));
        given(productQueryDao.findIngredientValidate(any(Long.class), anyMap())).willReturn(validates);

        //when
        productQueryService.validateOrder(command);

        //then
    }

    private OptionGroupValidateCommand getOptionGroupValidateCommand() {
        OptionValidateCommand optionA = OptionValidateCommand.builder()
                .id(1L)
                .name("optionA")
                .price(1000)
                .build();

        return OptionGroupValidateCommand.builder()
                .id(1L)
                .name("optionGroup")
                .options(List.of(optionA))
                .build();
    }

    private List<IngredientGroupValidateCommand> getIngredientGroupValidateCommands() {
        IngredientValidateCommand commandA = IngredientValidateCommand.builder()
                .id(1L)
                .name("ingredientA")
                .price(1000)
                .build();
        IngredientValidateCommand commandB = IngredientValidateCommand.builder()
                .id(2L)
                .name("ingredientB")
                .price(2000)
                .build();
        IngredientValidateCommand commandC = IngredientValidateCommand.builder()
                .id(3L)
                .name("ingredientC")
                .price(3000)
                .build();

        IngredientGroupValidateCommand groupA = IngredientGroupValidateCommand.builder()
                .id(1L)
                .name("groupA")
                .ingredients(List.of(commandA, commandB))
                .build();

        IngredientGroupValidateCommand groupB = IngredientGroupValidateCommand.builder()
                .id(2L)
                .name("groupB")
                .ingredients(List.of(commandA, commandC))
                .build();
        List<IngredientGroupValidateCommand> ingredientGroups = List.of(groupA, groupB);
        return ingredientGroups;
    }
}