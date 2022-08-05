package be.shop.slow_delivery.shop.infra;

import be.shop.slow_delivery.shop.application.dto.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static be.shop.slow_delivery.file.domain.QFile.file;
import static be.shop.slow_delivery.shop.domain.QOrderAmountDeliveryFee.orderAmountDeliveryFee;
import static be.shop.slow_delivery.shop.domain.QShop.shop;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
@Repository
public class ShopQueryDao {
    private final JPAQueryFactory queryFactory;

    public Optional<ShopSimpleInfo> findSimpleInfo(long shopId){
        return Optional.ofNullable(
                queryFactory
                        .from(shop)
                        .where(shop.id.eq(shopId))
                        .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                        .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                        .transform(
                                groupBy(shop.id).as(
                                        new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
                                                list(orderAmountDeliveryFee.fee.value))
                                )
                        ).get(shopId)
        );
    }

    public Optional<ShopDetailInfo> findDetailInfo(long shopId) {
        return Optional.ofNullable(
                queryFactory
                        .from(shop)
                        .where(shop.id.eq(shopId))
                        .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                        .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                        .transform(
                                groupBy(shop.id).as(
                                        new QShopDetailInfo(shop, file.filePath, list(orderAmountDeliveryFee.fee.value))
                                )
                        ).get(shopId)
        );
    }

    public ShopListQueryResult findShopListByCategory(long categoryId) {
        List<ShopSimpleInfo> shopSimpleInfoList = queryFactory
                .from(shop)
                .where(shop.categoryIds.contains(categoryId))
                .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                .transform(
                        groupBy(shop).list(new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
                                list(orderAmountDeliveryFee.fee.value)))
                );

        return new ShopListQueryResult(shopSimpleInfoList);
    }
}
