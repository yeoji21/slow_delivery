package be.shop.slow_delivery.product.infra;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.config.JpaQueryFactoryConfig;
import be.shop.slow_delivery.product.application.dto.ProductDetailInfo;
import be.shop.slow_delivery.product.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

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

        IngredientGroup groupA = new IngredientGroup("groupA", new SelectCount(1, 1));
        IngredientGroup groupB = new IngredientGroup("groupB", new SelectCount(1, 2));
        em.persist(groupA);
        em.persist(groupB);

        product.addIngredientGroup(groupA, 1);
        product.addIngredientGroup(groupB, 2);

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
        em.persist(ingredientA);
        em.persist(ingredientB);
        em.persist(ingredientC);

        groupA.addIngredient(ingredientA, 1);
        groupB.addIngredient(ingredientB, 1);
        groupB.addIngredient(ingredientC, 2);

        OptionGroup group = new OptionGroup("groupB", new Quantity(10));
        em.persist(group);

        em.persist(new ProductOptionGroup(product, group, 2));

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

        group.addOption(optionA, 1);
        group.addOption(optionB, 2);
        group.addOption(optionC, 3);

        em.flush();
        em.clear();

        //when
        ProductDetailInfo productDetailInfo = productQueryDao.findProductDetailInfo(product.getId());

        //then
        assertThat(productDetailInfo.getProductId()).isEqualTo(product.getId());
        assertThat(productDetailInfo.getName()).isEqualTo(product.getName());
        assertThat(productDetailInfo.getIngredientGroups().size()).isEqualTo(2);
    }
}