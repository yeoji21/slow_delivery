package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.criteria.*;
import be.shop.slow_delivery.product.infra.ProductQueryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceTest {
    private ProductCriteriaMapper mapper = ProductCriteriaMapper.INSTANCE;
    @Mock private ProductQueryDao productQueryDao;
    private ProductQueryService productQueryService;

    @BeforeEach
    void setUp() {
        productQueryService = new ProductQueryService(productQueryDao, mapper);
    }

    @Test
    void 상품_검증_테스트() throws Exception{
        //given
        ProductValidateCriteria criteria = ProductValidateCriteria.builder()
                .id(1L)
                .name("productA")
                .price(new Money(10_000))
                .orderQuantity(new Quantity(1))
                .ingredientGroups(getIngredientGroupValidateCommands())
                .optionGroups(List.of(getOptionGroupValidateCommand()))
                .build();

        given(productQueryDao.findProductValidate(any(Long.class), anyMap(), anyMap()))
                .willReturn(mapper.toProductValidate(criteria));

//        List<IngredientGroupValidate> validates = mapper.toIngredientValidate(criteria.getIngredientGroups());
//        ReflectionTestUtils.setField(validates.get(0).getSelectCount().getMaxCount(), "quantity", 2);
//        given(productQueryDao.findProductValidate(any(Long.class)))
//                .willReturn(Optional.ofNullable(mapper.toProductValidate(criteria)));
//        given(productQueryDao.findIngredientValidate(any(Long.class), anyMap()))
//                .willReturn(validates);
//        given(productQueryDao.findOptionValidate(any(Long.class), anyMap()))
//                .willReturn(mapper.toOptionValidate(criteria.getOptionGroups()));

        //when
        productQueryService.validateOrder(criteria);

        //then
    }

    private OptionGroupValidateCriteria getOptionGroupValidateCommand() {
        OptionValidateCriteria optionA = OptionValidateCriteria.builder()
                .id(1L)
                .name("optionA")
                .price(1000)
                .build();

        return OptionGroupValidateCriteria.builder()
                .id(1L)
                .name("optionGroup")
                .options(List.of(optionA))
                .build();
    }

    private List<IngredientGroupValidateCriteria> getIngredientGroupValidateCommands() {
        IngredientValidateCriteria commandA = IngredientValidateCriteria.builder()
                .id(1L)
                .name("ingredientA")
                .price(1000)
                .build();
        IngredientValidateCriteria commandB = IngredientValidateCriteria.builder()
                .id(2L)
                .name("ingredientB")
                .price(2000)
                .build();
        IngredientValidateCriteria commandC = IngredientValidateCriteria.builder()
                .id(3L)
                .name("ingredientC")
                .price(3000)
                .build();

        IngredientGroupValidateCriteria groupA = IngredientGroupValidateCriteria.builder()
                .id(1L)
                .name("groupA")
                .ingredients(List.of(commandA, commandB))
                .build();

        IngredientGroupValidateCriteria groupB = IngredientGroupValidateCriteria.builder()
                .id(2L)
                .name("groupB")
                .ingredients(List.of(commandA, commandC))
                .build();
        List<IngredientGroupValidateCriteria> ingredientGroups = List.of(groupA, groupB);
        return ingredientGroups;
    }
}