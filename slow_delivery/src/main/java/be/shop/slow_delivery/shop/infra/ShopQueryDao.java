package be.shop.slow_delivery.shop.infra;

import be.shop.slow_delivery.shop.application.dto.QShopDetailInfo;
import be.shop.slow_delivery.shop.application.dto.QShopSimpleInfo;
import be.shop.slow_delivery.shop.application.dto.ShopDetailInfo;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
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
        Map<Long, ShopSimpleInfo> deliveryFees =
                queryFactory
                        .from(shop)
                        .where(shop.id.eq(shopId))
                        .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                        .join(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                        .transform(
                                groupBy(shop.id).as(
                                        new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
                                                list(orderAmountDeliveryFee.fee.value))
                                )
                        );
        return Optional.ofNullable(deliveryFees.get(shopId));
    }

    public Optional<ShopDetailInfo> findDetailInfo(long shopId) {
        Map<Long, ShopDetailInfo> deliveryFees =
                queryFactory
                        .from(shop)
                        .where(shop.id.eq(shopId))
                        .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                        .join(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                        .transform(
                                groupBy(shop.id).as(
                                        new QShopDetailInfo(shop, file.filePath, list(orderAmountDeliveryFee.fee.value))
                                )
                        );
        return Optional.ofNullable(deliveryFees.get(shopId));
    }
}
