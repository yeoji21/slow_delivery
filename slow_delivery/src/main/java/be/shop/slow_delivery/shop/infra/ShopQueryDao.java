package be.shop.slow_delivery.shop.infra;

import be.shop.slow_delivery.shop.application.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.shop.slow_delivery.file.domain.QFile.file;
import static be.shop.slow_delivery.shop.domain.QCategoryShop.categoryShop;
import static be.shop.slow_delivery.shop.domain.QOrderAmountDeliveryFee.orderAmountDeliveryFee;
import static be.shop.slow_delivery.shop.domain.QShop.shop;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
@Repository
public class ShopQueryDao {
    private final JPAQueryFactory queryFactory;

    // 단건 가게 간략 정보
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

    // 단건 가게 상세 정보
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

    // 카테고리별 가게 목록 (간략 정보) -> 기본순 (shopId로 정렬)
    public ShopListQueryResult findByCategory(long categoryId, Long cursorShopId, int size) {
        List<ShopSimpleInfo> infoList = queryFactory
                .from(shop)
                .innerJoin(categoryShop).on(categoryShop.shop.eq(shop))
                .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                .where(categoryShop.categoryId.eq(categoryId), isShopIdLt(cursorShopId))
                .limit(size + 1)
                .orderBy(shop.id.desc())
                .transform(
                        groupBy(shop).list(new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
                                list(orderAmountDeliveryFee.fee.value)))
                );

        return new ShopListQueryResult(infoList, size);
    }

    // 카테고리별 가게 목록 (간략 정보) -> 배달비 낮은 순 (가게별 기본 배달비 중 가장 낮은 배달비를 기준으로 정렬)
    public ShopListQueryResult findByCategoryOrderByDeliveryFee(long categoryId, DeliveryFeeCursor cursor, int size) {
            List<Long> shopIds = queryFactory
                .select(orderAmountDeliveryFee.shop.id, orderAmountDeliveryFee.fee.value.min())
                .from(orderAmountDeliveryFee)
                .join(categoryShop).on(categoryShop.shop.eq(orderAmountDeliveryFee.shop))
                .groupBy(orderAmountDeliveryFee.shop)
                .having(cursorCondition(cursor.getFee(), cursor.getShopId()))
                .where(categoryShop.categoryId.eq(categoryId))
                .orderBy(orderAmountDeliveryFee.fee.value.min().asc())
                .limit(size + 1)
                .fetch()
                .stream()
                .map(t -> t.get(orderAmountDeliveryFee.shop.id))
                .collect(Collectors.toList());

        List<ShopSimpleInfo> infoList = queryFactory
                .from(shop)
                .innerJoin(categoryShop).on(categoryShop.shop.eq(shop))
                .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                .where(shop.id.in(shopIds))
                .orderBy(shop.id.desc())
                .transform(
                        groupBy(shop).list(new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
                                list(orderAmountDeliveryFee.fee.value)))
                );

        return new ShopListQueryResult(infoList, size);
    }

    private BooleanExpression cursorCondition(Integer cursorFee, Long cursorShopId) {
        return deliveryFeeGt(cursorFee) != null && deliveryFeeEqShopIdGt(cursorFee, cursorShopId) != null ?
                deliveryFeeGt(cursorFee).or(deliveryFeeEqShopIdGt(cursorFee, cursorShopId)) : null;
    }

    private BooleanExpression deliveryFeeEqShopIdGt(Integer cursorFee, Long cursorShopId) {
        return cursorFee != null && cursorShopId != null ?
                orderAmountDeliveryFee.fee.value.min().eq(cursorFee).and(orderAmountDeliveryFee.shop.id.gt(cursorShopId)) : null;
    }

    private BooleanExpression deliveryFeeGt(Integer cursorFee) {
        return cursorFee != null ? orderAmountDeliveryFee.fee.value.min().gt(cursorFee) : null;
    }


    private BooleanExpression isShopIdLt(Long cursorId) {
        return cursorId != null ? shop.id.lt(cursorId) : null;
    }
}
