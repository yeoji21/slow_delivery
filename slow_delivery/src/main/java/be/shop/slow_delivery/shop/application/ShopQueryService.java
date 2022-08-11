package be.shop.slow_delivery.shop.application;

import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.shop.application.dto.ShopDetailInfo;
import be.shop.slow_delivery.shop.application.dto.ShopListQueryResult;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import be.shop.slow_delivery.shop.infra.ShopQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static be.shop.slow_delivery.exception.ErrorCode.SHOP_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ShopQueryService {
    private final ShopQueryDao shopQueryDao;

    @Transactional(readOnly = true)
    public ShopSimpleInfo findSimpleInfo(long shopId) {
        return shopQueryDao.findSimpleInfo(shopId)
                .orElseThrow(() -> new NotFoundException(SHOP_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ShopDetailInfo findDetailInfo(long shopId) {
        return shopQueryDao.findDetailInfo(shopId)
                .orElseThrow(() -> new NotFoundException(SHOP_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ShopListQueryResult findShopsOrderByNewest(long categoryId, String cursor, int size) {
        return shopQueryDao.findByCategoryOrderByNewest(categoryId, cursor, size);
    }

    @Transactional(readOnly = true)
    public ShopListQueryResult findShopsOrderByDeliveryFee(long categoryId, String cursor, int size) {
        return shopQueryDao.findByCategoryOrderByDeliveryFee(categoryId, cursor, size);
    }
}
