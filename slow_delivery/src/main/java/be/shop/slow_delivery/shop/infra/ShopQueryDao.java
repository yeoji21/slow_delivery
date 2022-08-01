package be.shop.slow_delivery.shop.infra;

import be.shop.slow_delivery.shop.application.dto.QShopSimpleInfo;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static be.shop.slow_delivery.file.domain.QFile.file;
import static be.shop.slow_delivery.shop.domain.QShop.shop;

@RequiredArgsConstructor
@Repository
public class ShopQueryDao {
    private final JPAQueryFactory queryFactory;

    public Optional<ShopSimpleInfo> findSimpleInfo(long shopId){
        return Optional.ofNullable(
                queryFactory.select(new QShopSimpleInfo(shop, file.filePath))
                        .from(shop)
                        .leftJoin(file).on(shop.shopThumbnailFileId.eq(file.id))
                        .where(shop.id.eq(shopId))
                        .fetchOne()
        );
    }
}
