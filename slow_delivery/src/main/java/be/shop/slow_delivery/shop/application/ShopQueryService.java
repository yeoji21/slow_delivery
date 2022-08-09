package be.shop.slow_delivery.shop.application;

import be.shop.slow_delivery.shop.application.dto.ShopListQueryResult;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import be.shop.slow_delivery.shop.application.dto.ShopDetailInfo;
import be.shop.slow_delivery.shop.infra.ShopQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShopQueryService {
    private final ShopQueryDao shopQueryDao;

    @Transactional(readOnly = true)
    public ShopSimpleInfo findSimpleInfo(long shopId) {
        // TODO: 2022/08/09 exception
        return shopQueryDao.findSimpleInfo(shopId)
                .orElseThrow(() -> new IllegalArgumentException("invalid shopId"));
    }

    @Transactional(readOnly = true)
    public ShopDetailInfo findDetailInfo(long shopId) {
        // TODO: 2022/08/09 exception
        return shopQueryDao.findDetailInfo(shopId)
                .orElseThrow(() -> new IllegalArgumentException("invalid shopId"));
    }

    @Transactional(readOnly = true)
    public ShopListQueryResult findShopListByCategory(long categoryId, String order, String cursor, int size) {
        // TODO: 2022/08/09 exception
        if(order.equals("newest"))
            return shopQueryDao.findByCategory(categoryId, cursor, size);
        else if(order.equals("delivery-fee"))
            return shopQueryDao.findByCategoryOrderByDeliveryFee(categoryId, cursor, size);
        else
            throw new IllegalArgumentException();
    }
}
