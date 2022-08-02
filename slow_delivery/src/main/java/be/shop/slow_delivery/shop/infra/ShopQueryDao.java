package be.shop.slow_delivery.shop.infra;

import be.shop.slow_delivery.shop.application.dto.QShopSimpleInfo;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.shop.slow_delivery.file.domain.QFile.file;
import static be.shop.slow_delivery.shop.domain.QOrderAmountDeliveryFee.orderAmountDeliveryFee;
import static be.shop.slow_delivery.shop.domain.QShop.shop;

@RequiredArgsConstructor
@Repository
public class ShopQueryDao {
    private final JPAQueryFactory queryFactory;

    public Optional<ShopSimpleInfo> findSimpleInfo(long shopId){
        Optional<ShopSimpleInfo> shopSimpleInfo =
                Optional.ofNullable(
                        queryFactory.select(new QShopSimpleInfo(shop, file.filePath))
                                .from(shop)
                                .leftJoin(file).on(shop.shopThumbnailFileId.eq(file.id))
                                .where(shop.id.eq(shopId))
                                .fetchOne()
                );

        shopSimpleInfo.ifPresent(
                info -> {
                    List<Integer> deliveryFees =
                            queryFactory.select(orderAmountDeliveryFee)
                                    .from(orderAmountDeliveryFee)
                                    .where(orderAmountDeliveryFee.shop.id.eq(info.getShopId()))
                                    .orderBy(orderAmountDeliveryFee.orderAmount.value.asc())
                                    .fetch()
                                    .stream()
                                    .map(deliveryFee -> deliveryFee.getFee().toInt())
                                    .collect(Collectors.toList());

                    info.setDefaultDeliveryFees(deliveryFees);
                }
        );

        return shopSimpleInfo;
    }
}
