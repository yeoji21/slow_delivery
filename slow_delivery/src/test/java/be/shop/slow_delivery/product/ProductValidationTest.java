package be.shop.slow_delivery.product;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.ProductQueryService;
import be.shop.slow_delivery.product.application.criteria.*;
import be.shop.slow_delivery.product.domain.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Disabled
@SpringBootTest
public class ProductValidationTest {
    @Autowired
    private ProductQueryService productQueryService;
    @Autowired
    private EntityManager em;

    @Test @Transactional
    void 상품_검증_통합_테스트() throws Exception{
        //given
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();
        em.persist(product);

        OptionGroup optionGroupA = new OptionGroup("groupA", new Quantity(3));
        OptionGroup optionGroupB = new OptionGroup("groupB", new Quantity(5));
        em.persist(optionGroupA);
        em.persist(optionGroupB);

        em.persist(new ProductOptionGroup(product, optionGroupA, 1));
        em.persist(new ProductOptionGroup(product, optionGroupB, 2));

        Option optionA = Option.builder()
                .stockId(0L)
                .name("optionA")
                .price(new Money(100))
                .build();

        Option optionB = Option.builder()
                .stockId(0L)
                .name("optionB")
                .price(new Money(500))
                .build();

        Option optionC = Option.builder()
                .stockId(0L)
                .name("optionC")
                .price(new Money(1000))
                .build();

        em.persist(optionA);
        em.persist(optionB);
        em.persist(optionC);

        em.persist(new OptionInGroup(optionGroupA, optionA, 1));
        em.persist(new OptionInGroup(optionGroupA, optionB, 2));
        em.persist(new OptionInGroup(optionGroupA, optionC, 3));

        em.persist(new OptionInGroup(optionGroupB, optionA, 1));

        OptionValidateCriteria ocA = OptionValidateCriteria.builder()
                .id(optionA.getId())
                .name(optionA.getName())
                .price(optionA.getPrice().toInt())
                .build();
        OptionValidateCriteria ocB = OptionValidateCriteria.builder()
                .id(optionB.getId())
                .name(optionB.getName())
                .price(optionB.getPrice().toInt())
                .build();
        OptionValidateCriteria ocC = OptionValidateCriteria.builder()
                .id(optionC.getId())
                .name(optionC.getName())
                .price(optionC.getPrice().toInt())
                .build();

        OptionGroupValidateCriteria gcA = OptionGroupValidateCriteria.builder()
                .id(optionGroupA.getId())
                .name(optionGroupA.getName())
                .options(List.of(ocA, ocB, ocC))
                .build();

        Ingredient ingredientA = Ingredient.builder()
                .stockId(0L)
                .name("ingredientA")
                .price(new Money(1000))
                .build();
        Ingredient ingredientB = Ingredient.builder()
                .stockId(0L)
                .name("ingredientB")
                .price(new Money(2000))
                .build();
        Ingredient ingredientC = Ingredient.builder()
                .stockId(0L)
                .name("ingredientC")
                .price(new Money(3000))
                .build();

        em.persist(ingredientA);
        em.persist(ingredientB);
        em.persist(ingredientC);

        IngredientGroup ingredientGroupA = new IngredientGroup("groupA", new SelectCount(1, 2));
        IngredientGroup ingredientGroupB = new IngredientGroup("groupB", new SelectCount(1, 2));
        em.persist(ingredientGroupA);
        em.persist(ingredientGroupB);

        em.persist(new ProductIngredientGroup(product, ingredientGroupA, 1));
        em.persist(new ProductIngredientGroup(product, ingredientGroupB, 2));

        em.persist(new IngredientInGroup(ingredientGroupA, ingredientA, 1));
        em.persist(new IngredientInGroup(ingredientGroupA, ingredientB, 2));

        em.persist(new IngredientInGroup(ingredientGroupB, ingredientA, 1));
        em.persist(new IngredientInGroup(ingredientGroupB, ingredientC, 2));

        em.flush();
        em.clear();

        IngredientValidateCriteria commandA = IngredientValidateCriteria.builder()
                .id(ingredientA.getId())
                .name(ingredientA.getName())
                .price(ingredientA.getPrice().toInt())
                .build();
        IngredientValidateCriteria commandB = IngredientValidateCriteria.builder()
                .id(ingredientB.getId())
                .name(ingredientB.getName())
                .price(ingredientB.getPrice().toInt())
                .build();
        IngredientValidateCriteria commandC = IngredientValidateCriteria.builder()
                .id(ingredientC.getId())
                .name(ingredientC.getName())
                .price(ingredientC.getPrice().toInt())
                .build();

        IngredientGroupValidateCriteria commandGroupA = IngredientGroupValidateCriteria.builder()
                .id(ingredientGroupA.getId())
                .name(ingredientGroupA.getName())
                .ingredients(List.of(commandA, commandB))
                .build();

        IngredientGroupValidateCriteria commandGroupB = IngredientGroupValidateCriteria.builder()
                .id(ingredientGroupB.getId())
                .name(ingredientGroupB.getName())
                .ingredients(List.of(commandA, commandC))
                .build();

        ProductValidateCriteria criteria = ProductValidateCriteria.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .orderQuantity(new Quantity(1))
                .ingredientGroups(List.of(commandGroupA, commandGroupB))
                .optionGroups(List.of(gcA))
                .build();

        //when
        productQueryService.validateOrder(criteria);

        //then

    }
}
