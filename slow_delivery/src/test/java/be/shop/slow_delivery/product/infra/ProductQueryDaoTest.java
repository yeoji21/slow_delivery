package be.shop.slow_delivery.product.infra;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.config.JpaQueryFactoryConfig;
import be.shop.slow_delivery.product.application.criteria.*;
import be.shop.slow_delivery.product.application.query.ProductDetailInfo;
import be.shop.slow_delivery.product.domain.*;
import be.shop.slow_delivery.product.domain.validate.ProductValidate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Import({JpaQueryFactoryConfig.class, ProductQueryDao.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductQueryDaoTest {
    @Autowired
    private ProductQueryDao productQueryDao;
    @Autowired
    private EntityManager em;

    @Test
    void 상품_검증_데이터_조회() throws Exception{
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
        ProductValidate productValidate = productQueryDao.findProductValidate(criteria.getId(),
                criteria.getIngredientIdMap(), criteria.getOptionIdMap());

        //then
        assertThat(productValidate.getId()).isGreaterThan(0L);
        assertThat(productValidate.getName()).isNotBlank();
        assertThat(productValidate.getPrice()).isNotNull();
        assertThat(productValidate.getIngredientGroupValidates().size()).isGreaterThan(0);
        assertThat(productValidate.getOptionGroupValidates().size()).isGreaterThan(0);

        productValidate.getIngredientGroupValidates()
                .forEach(group -> {
                    assertThat(group.getId()).isGreaterThan(0L);
                    assertThat(group.getName()).isNotBlank();
                    assertThat(group.getSelectCount()).isNotNull();
                    assertThat(group.getIngredients().size()).isGreaterThan(0);
                    group.getIngredients().forEach(i -> assertThat(i.getId()).isGreaterThan(0L));
                });

        productValidate.getOptionGroupValidates()
                .forEach(group -> {
                    assertThat(group.getId()).isGreaterThan(0L);
                    assertThat(group.getName()).isNotBlank();
                    assertThat(group.getMaxSelectCount()).isNotNull();
                    assertThat(group.getOptions().size()).isGreaterThan(0);
                    group.getOptions().forEach(o -> assertThat(o.getId()).isGreaterThan(0L));
                });
    }

    @Test
    void findProductDetailInfo() throws Exception{
        //given
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();
        em.persist(product);
        setIngredients(product);
        setOptions(product);
        em.flush();
        em.clear();

        //when
        ProductDetailInfo productDetailInfo = productQueryDao.findProductDetailInfo(product.getId());

        //then
        assertThat(productDetailInfo.getProductId()).isEqualTo(product.getId());
        assertThat(productDetailInfo.getName()).isEqualTo(product.getName());
        assertThat(productDetailInfo.getIngredientGroups().size()).isEqualTo(2);
        assertThat(productDetailInfo.getIngredientGroups().get(0).getIngredients().size()).isEqualTo(1);
        assertThat(productDetailInfo.getIngredientGroups().get(1).getIngredients().size()).isEqualTo(2);
        assertThat(productDetailInfo.getOptionGroups().size()).isEqualTo(1);
        assertThat(productDetailInfo.getOptionGroups().get(0).getOptions().size()).isEqualTo(3);
    }

    private void setIngredients(Product product) {
        IngredientGroup ingredientGroupA = new IngredientGroup("groupA", new SelectCount(1, 1));
        IngredientGroup ingredientGroupB = new IngredientGroup("groupB", new SelectCount(1, 2));
        em.persist(ingredientGroupA);
        em.persist(ingredientGroupB);

        em.persist(new ProductIngredientGroup(product, ingredientGroupA, 1));
        em.persist(new ProductIngredientGroup(product, ingredientGroupB, 2));

        Ingredient ingredientA = Ingredient.builder()
                .stockId(2L)
                .name("ingredientA")
                .price(new Money(1000))
                .build();
        Ingredient ingredientB = Ingredient.builder()
                .stockId(3L)
                .name("ingredientB")
                .price(new Money(2000))
                .build();
        Ingredient ingredientC = Ingredient.builder()
                .stockId(4L)
                .name("ingredientC")
                .price(new Money(3000))
                .build();
        Ingredient hidingIngredient = Ingredient.builder()
                .stockId(4L)
                .name("hidingIngredient")
                .price(new Money(3000))
                .build();
        em.persist(ingredientA);
        em.persist(ingredientB);
        em.persist(ingredientC);
        em.persist(hidingIngredient);

        em.persist(new IngredientInGroup(ingredientGroupA, ingredientA, 1));
        em.persist(new IngredientInGroup(ingredientGroupA, hidingIngredient, 2));

        em.persist(new IngredientInGroup(ingredientGroupB, ingredientB, 1));
        em.persist(new IngredientInGroup(ingredientGroupB, ingredientC, 2));

        em.createQuery("update IngredientInGroup ig set ig.displayInfo.isDisplay = false where ig.ingredient.id =: igId")
                .setParameter("igId", hidingIngredient.getId())
                .executeUpdate();
    }

    private void setOptions(Product product) {
        OptionGroup optionGroup = new OptionGroup("groupB", new Quantity(10));
        em.persist(optionGroup);

        OptionGroup hidingOptionGroup = new OptionGroup("groupB", new Quantity(10));
        em.persist(hidingOptionGroup);

        em.persist(new ProductOptionGroup(product, optionGroup, 1));
        em.persist(new ProductOptionGroup(product, hidingOptionGroup, 2));

        em.createQuery("update ProductOptionGroup pog set pog.displayInfo.isDisplay = false where pog.id =: pogId")
                .setParameter("pogId", hidingOptionGroup.getId())
                .executeUpdate();

        Option optionA = Option.builder()
                .stockId(2L)
                .name("optionA")
                .price(new Money(1000))
                .build();
        Option optionB = Option.builder()
                .stockId(3L)
                .name("optionB")
                .price(new Money(2000))
                .build();
        Option optionC = Option.builder()
                .stockId(4L)
                .name("optionC")
                .price(new Money(3000))
                .build();
        em.persist(optionA);
        em.persist(optionB);
        em.persist(optionC);

        em.persist(new OptionInGroup(optionGroup, optionA, 1));
        em.persist(new OptionInGroup(optionGroup, optionB, 2));
        em.persist(new OptionInGroup(optionGroup, optionC, 3));
    }
}