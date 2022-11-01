package be.shop.slow_delivery.shop.infra;

import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.shop.application.dto.*;
import be.shop.slow_delivery.shop.domain.OrderAmountDeliveryFee;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.shop.slow_delivery.category.domain.QCategory.category;
import static be.shop.slow_delivery.exception.ErrorCode.SHOP_NOT_FOUND;
import static be.shop.slow_delivery.file.domain.QFile.file;
import static be.shop.slow_delivery.shop.domain.QCategoryShop.categoryShop;
import static be.shop.slow_delivery.shop.domain.QOrderAmountDeliveryFee.orderAmountDeliveryFee;
import static be.shop.slow_delivery.shop.domain.QShop.shop;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static java.util.stream.Collectors.groupingBy;

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
                        .leftJoin(file).on(file.id.eq(shop.thumbnailFileId))
                        .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                        .transform(
                                groupBy(shop.id).as(
                                        new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.amount, file.filePath,
                                                list(orderAmountDeliveryFee.fee.amount))
                                )
                        ).get(shopId)
        );
    }

    // 단건 가게 상세 정보
    public Optional<ShopDetailInfo> findDetailInfo(long shopId) {
        List<String> categories = queryFactory.select(category.categoryName.stringValue())
                .from(categoryShop)
                .join(category).on(categoryShop.categoryId.eq(category.id))
                .where(categoryShop.shop.id.eq(shopId))
                .fetch();

        List<DeliveryFeeInfo> deliveryFees = queryFactory.select(new QDeliveryFeeInfo(
                        orderAmountDeliveryFee.orderAmount.amount,
                        orderAmountDeliveryFee.fee.amount))
                .from(orderAmountDeliveryFee)
                .where(orderAmountDeliveryFee.shop.eq(shop))
                .fetch();

        ShopDetailInfo shopDetailInfo =
                queryFactory.select(new QShopDetailInfo(shop, file.filePath))
                        .from(shop)
                        .leftJoin(file).on(file.id.eq(shop.thumbnailFileId))
                        .where(shop.id.eq(shopId))
                        .fetchOne();

        shopDetailInfo.setCategories(categories);
        shopDetailInfo.setDeliveryFees(deliveryFees);
        return Optional.of(shopDetailInfo);
    }

    // 카테고리별 가게 목록 (간략 정보) -> 기본순 (shopId로 정렬)
    public ShopListQueryResult findByCategoryOrderByNewest(long categoryId, String cursor, int size) {
        List<ShopSimpleInfo> infoList = queryFactory
                .select(new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.amount, file.filePath))
                .from(shop)
                .innerJoin(categoryShop).on(categoryShop.shop.eq(shop))
                .leftJoin(file).on(file.id.eq(shop.thumbnailFileId))
                .where(categoryShop.categoryId.eq(categoryId), shopIdCursorCondition(cursor))
                .limit(size + 1)
                .orderBy(shop.id.desc())
                .fetch();
        hasDataCheck(infoList);

        setDeliveryFees(infoList);
        boolean hasNext = hasNext(size, infoList);
        String nextCursor = hasNext ? getShopIdNextCursor(infoList) : null;
        return new ShopListQueryResult(infoList, hasNext, nextCursor);
    }

    // 카테고리별 가게 목록 (간략 정보) -> 배달비 낮은 순 (가게별 기본 배달비 중 가장 낮은 배달비를 기준으로 정렬)
    public ShopListQueryResult findByCategoryOrderByDeliveryFee(long categoryId, String cursor, int size) {
        List<Long> shopIds = queryFactory
                .select(orderAmountDeliveryFee.shop.id, orderAmountDeliveryFee.fee.amount.min())
                .from(orderAmountDeliveryFee)
                .join(categoryShop).on(categoryShop.shop.eq(orderAmountDeliveryFee.shop))
                .groupBy(orderAmountDeliveryFee.shop)
                .having(deliveryFeeCursorCondition(cursor))
                .where(categoryShop.categoryId.eq(categoryId))
                .orderBy(orderAmountDeliveryFee.fee.amount.min().asc())
                .limit(size + 1)
                .fetch()
                .stream()
                .map(t -> t.get(orderAmountDeliveryFee.shop.id))
                .collect(Collectors.toList());

        List<ShopSimpleInfo> infoList = queryFactory
                .from(shop)
                .innerJoin(categoryShop).on(categoryShop.shop.eq(shop))
                .leftJoin(file).on(file.id.eq(shop.thumbnailFileId))
                .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                .where(shop.id.in(shopIds))
                .transform(
                        groupBy(shop).list(new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.amount, file.filePath,
                                list(orderAmountDeliveryFee.fee.amount)))
                );

        hasDataCheck(infoList);

        infoList.sort(Comparator.comparingLong(info -> shopIds.indexOf(info.getShopId())));
        boolean hasNext = hasNext(size, infoList);
        String nextCursor = hasNext ? getDeliveryFeeNextCursor(infoList) : null;
        return new ShopListQueryResult(infoList, hasNext, nextCursor);
    }
    private String getDeliveryFeeNextCursor(List<ShopSimpleInfo> infoList) {
        ShopSimpleInfo lastOne = infoList.get(infoList.size() - 1);

        Integer lastFee = lastOne.getDefaultDeliveryFees()
                .stream()
                .min(Comparator.comparing((Integer fee) -> fee))
                .get();

        long lastShopId = lastOne.getShopId();
        return String.format("%010d", lastFee).concat(String.format("%010d", lastShopId));
    }

    private String getShopIdNextCursor(List<ShopSimpleInfo> infoList) {
        long lastShopId = infoList.get(infoList.size() - 1).getShopId();
        return String.format("%020d", lastShopId);
    }

    private boolean hasNext(int size, List<ShopSimpleInfo> infoList) {
        boolean hasNext = false;
        if (infoList.size() > size) {
            infoList.remove(size);
            hasNext = true;
        }
        return hasNext;
    }

    private void setDeliveryFees(List<ShopSimpleInfo> infoList) {
        Map<Long, List<OrderAmountDeliveryFee>> deliveryFeeMap = findDeliveryFeeMap(infoList);

        infoList.forEach(
                info -> {
                    List<Integer> deliveryFees = deliveryFeeMap.get(info.getShopId())
                            .stream()
                            .map(deliveryFee -> deliveryFee.getFee().toInt())
                            .collect(Collectors.toList());
                    info.setDefaultDeliveryFees(deliveryFees);
                }
        );
    }

    private void hasDataCheck(List<ShopSimpleInfo> infoList) {
        if(infoList.size() == 0)
            throw new NotFoundException(SHOP_NOT_FOUND);
    }

    private Map<Long, List<OrderAmountDeliveryFee>> findDeliveryFeeMap(List<ShopSimpleInfo> infoList) {
        List<Long> shopIds = infoList.stream()
                .map(ShopSimpleInfo::getShopId)
                .collect(Collectors.toList());

        return queryFactory
                .select(orderAmountDeliveryFee)
                .from(orderAmountDeliveryFee)
                .where(orderAmountDeliveryFee.shop.id.in(shopIds))
                .fetch()
                .stream()
                .collect(groupingBy(OrderAmountDeliveryFee::getShopId));
    }

    private BooleanExpression shopIdCursorCondition(String cursor) {
        if(cursorValidate(cursor)) return null;
        return StringExpressions.lpad(shop.id.stringValue(), 20, '0')
                .lt(cursor);
    }

    private BooleanExpression deliveryFeeCursorCondition(String cursor) {
        if(cursorValidate(cursor)) return null;
        return StringExpressions.lpad(orderAmountDeliveryFee.fee.amount.min().stringValue(), 10, '0')
                .concat(StringExpressions.lpad(orderAmountDeliveryFee.shop.id.stringValue(), 10, '0'))
                .gt(cursor);
    }

    private boolean cursorValidate(String cursor) {
        return cursor == null || cursor.length() < 20;
    }
}
