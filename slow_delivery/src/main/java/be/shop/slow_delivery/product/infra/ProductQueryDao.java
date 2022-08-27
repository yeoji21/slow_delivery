package be.shop.slow_delivery.product.infra;

import be.shop.slow_delivery.product.application.dto.*;
import com.mysema.commons.lang.Assert;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public ProductDetailInfo findProductDetailInfo(long productId) {
        ProductDetailInfo productDetailInfo = queryFactory
                .select(new QProductDetailInfo(product.id, product.name, product.description,
                        product.price, product.maxOrderQuantity, product.thumbnailFileId))
                .from(product)
                .where(product.id.eq(productId), product.stockInfo.isSale.isTrue())
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
