package be.shop.slow_delivery.product.infra;

import be.shop.slow_delivery.product.domain.*;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static be.shop.slow_delivery.product.domain.QIngredient.ingredient;
import static be.shop.slow_delivery.product.domain.QIngredientGroup.ingredientGroup;
import static be.shop.slow_delivery.product.domain.QIngredientInGroup.ingredientInGroup;
import static be.shop.slow_delivery.product.domain.QOption.option;
import static be.shop.slow_delivery.product.domain.QOptionGroup.optionGroup;
import static be.shop.slow_delivery.product.domain.QOptionInGroup.optionInGroup;
import static be.shop.slow_delivery.product.domain.QProduct.product;
import static be.shop.slow_delivery.product.domain.QProductIngredientGroup.productIngredientGroup;
import static be.shop.slow_delivery.product.domain.QProductOptionGroup.productOptionGroup;
import static com.querydsl.core.group.GroupBy.groupBy;

@RequiredArgsConstructor
@Repository
public class ProductJpaRepository implements ProductRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public void save(Product product) {
        entityManager.persist(product);
    }

    @Override
    public Optional<Product> findById(long productId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(product)
                        .where(product.id.eq(productId))
                        .fetchOne()
        );
    }

    @Override
    public Map<IngredientGroup, List<Ingredient>> findIngredientsMap(long productId, List<Long> ingredientIds) {
        return queryFactory
                .from(productIngredientGroup)
                .innerJoin(productIngredientGroup.product, product)
                .innerJoin(productIngredientGroup.ingredientGroup, ingredientGroup)
                .leftJoin(ingredientInGroup).on(ingredientInGroup.ingredientGroup.eq(ingredientGroup))
                .leftJoin(ingredientInGroup.ingredient, ingredient).on(ingredient.id.in(ingredientIds))
                .where(product.id.eq(productId),
                        ingredientInGroup.displayInfo.isDisplay.isTrue(),
                        ingredient.isSale.isTrue())
                .transform(groupBy(ingredientGroup).as(GroupBy.list(ingredient)));
    }

    @Override
    public Map<IngredientGroup, List<Ingredient>> findIngredientsMap(long productId, Map<Long, List<Long>> ingredientIdMap) {
        // TODO: 2022/09/09 productIngredientGroup

        List<IngredientGroup> groups = queryFactory
                .select(ingredientGroup)
                .from(productIngredientGroup)
                .innerJoin(productIngredientGroup.product, product)
                .innerJoin(productIngredientGroup.ingredientGroup, ingredientGroup)
                .where(product.id.eq(productId))
                .fetch();

        List<Long> ingredientIds = ingredientIdMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<Ingredient> ingredients = queryFactory
                .select(ingredient).distinct()
                .from(ingredientInGroup)
                .leftJoin(ingredientInGroup).on(ingredientInGroup.ingredientGroup.in(groups))
                .leftJoin(ingredientInGroup.ingredient, ingredient).on(ingredient.id.in(ingredientIds))
                .where(
                        ingredientInGroup.displayInfo.isDisplay.isTrue(),
                        ingredient.isSale.isTrue())
                .fetch();

        Map<IngredientGroup, List<Ingredient>> result = new HashMap<>();
        for (IngredientGroup group : groups) {
            List<Ingredient> value = ingredients.stream()
                    .filter(i -> ingredientIdMap.get(group.getId()).contains(i.getId()))
                    .collect(Collectors.toList());
            result.put(group, value);
        }

        return result;
    }

    @Override
    public Map<OptionGroup, List<Option>> findOptionsMap(long productId, List<Long> optionIds) {
        return queryFactory
                .from(productOptionGroup)
                .innerJoin(productOptionGroup.product, product)
                .innerJoin(productOptionGroup.optionGroup, optionGroup)
                .leftJoin(optionInGroup).on(optionInGroup.optionGroup.eq(optionGroup))
                .leftJoin(optionInGroup.option, option)
                .where(product.id.eq(productId), option.id.in(optionIds),
                        productOptionGroup.displayInfo.isDisplay.isTrue(),
                        optionInGroup.displayInfo.isDisplay.isTrue(),
                        option.isSale.isTrue())
                .transform(groupBy(optionGroup).as(GroupBy.list(option)));
    }

}








































