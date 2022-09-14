package be.shop.slow_delivery.product.infra;

import be.shop.slow_delivery.product.application.query.*;
import be.shop.slow_delivery.product.domain.validate.*;
import com.mysema.commons.lang.Assert;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
@Repository
public class ProductQueryDao {
    private final JPAQueryFactory queryFactory;

    public ProductValidate findProductValidate(long productId,
                                               Map<Long, List<Long>> ingredientIdMap,
                                               Map<Long, List<Long>> optionIdMap) {
        ProductValidate productValidate = queryFactory
                .select(new QProductValidate(product.id, product.name, product.price, product.maxOrderQuantity))
                .from(product)
                .where(product.id.eq(productId),
                        product.isSale.isTrue())
                .fetchOne();
        Assert.notNull(productValidate, "productId " + productId + " is not found");
        productValidate.setIngredientGroupValidates(findIngredientValidate(productId, ingredientIdMap));
        productValidate.setOptionGroupValidates(findOptionValidate(productId, optionIdMap));
        return productValidate;
    }

    private List<IngredientGroupValidate> findIngredientValidate(long productId, Map<Long, List<Long>> ingredientIdMap) {
        List<Long> ingredientIds = ingredientIdMap.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<IngredientGroupValidate> list = queryFactory
                .from(productIngredientGroup)
                .innerJoin(productIngredientGroup.ingredientGroup, ingredientGroup)
                .innerJoin(ingredientInGroup).on(ingredientInGroup.ingredientGroup.eq(ingredientGroup))
                .leftJoin(ingredientInGroup.ingredient, ingredient).on(ingredient.id.in(ingredientIds))
                .where(productIngredientGroup.product.id.eq(productId),
                        ingredientInGroup.displayInfo.isDisplay.isTrue(),
                        ingredient.isSale.isTrue())
                .transform(groupBy(ingredientGroup)
                        .list(new QIngredientGroupValidate(ingredientGroup.id, ingredientGroup.name, ingredientGroup.selectCount,
                                list(new QIngredientGroupValidate_IngredientValidate(ingredient.id, ingredient.name, ingredient.price))
                        ))
                );

        list.forEach(group -> group.getIngredients()
                .removeIf(i -> !ingredientIdMap.get(group.getId()).contains(i.getId())));

        return list;
    }

    private List<OptionGroupValidate> findOptionValidate(long productId, Map<Long, List<Long>> optionIdMap) {
        List<Long> optionIds = optionIdMap.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<OptionGroupValidate> list = queryFactory
                .from(productOptionGroup)
                .innerJoin(productOptionGroup.optionGroup, optionGroup)
                .innerJoin(optionInGroup).on(optionInGroup.optionGroup.eq(optionGroup))
                .innerJoin(optionInGroup.option, option)
                .where(productOptionGroup.product.id.eq(productId),
                        optionGroup.id.in(optionIdMap.keySet()),
                        option.id.in(optionIds),
                        productOptionGroup.displayInfo.isDisplay.isTrue(),
                        optionInGroup.displayInfo.isDisplay.isTrue(),
                        option.isSale.isTrue())
                .transform(groupBy(optionGroup)
                        .list(new QOptionGroupValidate(optionGroup.id, optionGroup.name, optionGroup.maxSelectCount,
                                list(new QOptionGroupValidate_OptionValidate(option.id, option.name, option.price))))
                );

        list.forEach(group -> group.getOptions()
                .removeIf(i -> !optionIdMap.get(group.getId()).contains(i.getId())));

        return list;
    }

    public ProductDetailInfo findProductDetailInfo(long productId) {
        ProductDetailInfo productDetailInfo = queryFactory
                .select(new QProductDetailInfo(product.id, product.name, product.description,
                        product.price, product.maxOrderQuantity, product.thumbnailFileId, product.isSale))
                .from(product)
                .where(product.id.eq(productId), product.isSale.isTrue())
                .fetchOne();
        Assert.notNull(productDetailInfo, "productId " + productId + " is not found");

        List<IngredientGroupInfo> ingredientGroups = queryFactory
                .from(productIngredientGroup)
                .leftJoin(productIngredientGroup.ingredientGroup, ingredientGroup)
                .leftJoin(ingredientInGroup).on(ingredientInGroup.ingredientGroup.eq(ingredientGroup))
                .leftJoin(ingredientInGroup.ingredient, ingredient)
                .where(productIngredientGroup.product.id.eq(productId),
                        ingredientInGroup.displayInfo.isDisplay.isTrue())
                .orderBy(productIngredientGroup.displayOrder.displayOrder.asc(),
                        ingredientInGroup.displayInfo.displayOrder.displayOrder.asc())
                .transform(groupBy(ingredientGroup)
                        .list(new QIngredientGroupInfo(ingredientGroup.id, ingredientGroup.name,
                                ingredientGroup.selectCount.minCount, ingredientGroup.selectCount.maxCount,
                                list(new QIngredientInfo(ingredient.id, ingredient.name, ingredient.price)))
                        )
                );
        productDetailInfo.setIngredientGroups(ingredientGroups);

        List<OptionGroupInfo> optionGroups = queryFactory
                .from(productOptionGroup)
                .leftJoin(productOptionGroup.optionGroup, optionGroup)
                .leftJoin(optionInGroup).on(optionInGroup.optionGroup.eq(optionGroup))
                .leftJoin(optionInGroup.option, option)
                .where(productOptionGroup.product.id.eq(productId),
                        productOptionGroup.displayInfo.isDisplay.isTrue(),
                        optionInGroup.displayInfo.isDisplay.isTrue())
                .orderBy(productOptionGroup.displayInfo.displayOrder.displayOrder.asc(),
                        optionInGroup.displayInfo.displayOrder.displayOrder.asc())
                .transform(groupBy(optionGroup)
                        .list(new QOptionGroupInfo(optionGroup.id, optionGroup.name, optionGroup.maxSelectCount,
                                list(new QOptionInfo(option.id, option.name, option.price)))));
        productDetailInfo.setOptionGroups(optionGroups);

        return productDetailInfo;
    }
}
