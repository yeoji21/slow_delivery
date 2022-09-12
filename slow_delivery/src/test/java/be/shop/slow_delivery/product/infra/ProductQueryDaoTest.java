package be.shop.slow_delivery.product.infra;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.config.JpaQueryFactoryConfig;
import be.shop.slow_delivery.product.application.command.IngredientGroupValidateCommand;
import be.shop.slow_delivery.product.application.command.IngredientValidateCommand;
import be.shop.slow_delivery.product.application.command.ProductValidateCommand;
import be.shop.slow_delivery.product.application.query.ProductDetailInfo;
import be.shop.slow_delivery.product.domain.*;
import be.shop.slow_delivery.product.domain.validate.IngredientGroupValidate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Map;

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
    void findIngredientValidate() throws Exception{
        //given
        ProductValidateCommand command = getProductValidateCommand();
        Map<Long, List<Long>> ingredientIdMap = command.getIngredientIdMap();

        //when
        List<IngredientGroupValidate> ingredientValidate = productQueryDao.findIngredientValidate(command.getId(), ingredientIdMap);

        //then
        assertThat(ingredientValidate.size()).isEqualTo(2);
        assertThat(ingredientValidate.get(0).getIngredients().size()).isEqualTo(1);
        assertThat(ingredientValidate.get(1).getIngredients().size()).isEqualTo(2);
    }


    private ProductValidateCommand getProductValidateCommand() {
        Product product = Product.builder()
                .stockId(1L)
                .name("product A")
                .description("~~~")
                .price(new Money(10_000))
                .maxOrderQuantity(new Quantity(5))
                .build();
        em.persist(product);

        IngredientGroup groupA = new IngredientGroup("groupA", new SelectCount(1, 1));
        IngredientGroup groupB = new IngredientGroup("groupB", new SelectCount(1, 2));
        em.persist(groupA);
        em.persist(groupB);

        product.addIngredientGroup(groupA, 1);
        product.addIngredientGroup(groupB, 2);

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
        Ingredient notShownIngredient = Ingredient.builder()
                .stockId(0L)
                .name("nowShownIngredient")
                .price(new Money(500))
                .build();

        em.persist(ingredientA);
        em.persist(ingredientB);
        em.persist(ingredientC);
        em.persist(notShownIngredient);

        groupA.addIngredient(ingredientA, 1);
        groupA.addIngredient(notShownIngredient, 2);

        groupB.addIngredient(ingredientB, 1);
        groupB.addIngredient(ingredientC, 2);
        groupB.addIngredient(ingredientA, 3);

        em.createQuery("update IngredientInGroup ig set ig.displayInfo.isDisplay = false " +
                        "where ig.ingredientGroup.id =: groupId and ig.ingredient.id =: ingredientId")
                .setParameter("groupId", groupA.getId())
                .setParameter("ingredientId", notShownIngredient.getId())
                .executeUpdate();

        em.flush();
        em.clear();

        IngredientValidateCommand commandA = IngredientValidateCommand.builder()
                .id(ingredientA.getId())
                .name(ingredientA.getName())
                .price(ingredientA.getPrice().toInt())
                .build();
        IngredientValidateCommand commandB = IngredientValidateCommand.builder()
                .id(ingredientB.getId())
                .name(ingredientB.getName())
                .price(ingredientB.getPrice().toInt())
                .build();
        IngredientValidateCommand commandC = IngredientValidateCommand.builder()
                .id(ingredientC.getId())
                .name(ingredientC.getName())
                .price(ingredientC.getPrice().toInt())
                .build();
        IngredientValidateCommand notShownCommand = IngredientValidateCommand.builder()
                .id(notShownIngredient.getId())
                .name(notShownIngredient.getName())
                .price(notShownIngredient.getPrice().toInt())
                .build();

        IngredientGroupValidateCommand commandGroupA = IngredientGroupValidateCommand.builder()
                .id(groupA.getId())
                .name(groupA.getName())
                .ingredients(List.of(commandA, commandB, notShownCommand))
                .build();

        IngredientGroupValidateCommand commandGroupB = IngredientGroupValidateCommand.builder()
                .id(groupB.getId())
                .name(groupB.getName())
                .ingredients(List.of(commandA, commandC))
                .build();

        return ProductValidateCommand.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .orderQuantity(new Quantity(1))
                .ingredientGroups(List.of(commandGroupA, commandGroupB))
                .optionGroups(null)
                .build();
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

        product.addIngredientGroup(ingredientGroupA, 1);
        product.addIngredientGroup(ingredientGroupB, 2);

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

        ingredientGroupA.addIngredient(ingredientA, 1);
        ingredientGroupA.addIngredient(hidingIngredient, 2);
        ingredientGroupB.addIngredient(ingredientB, 1);
        ingredientGroupB.addIngredient(ingredientC, 2);

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

        optionGroup.addOption(optionA, 1);
        optionGroup.addOption(optionB, 2);
        optionGroup.addOption(optionC, 3);
    }
}