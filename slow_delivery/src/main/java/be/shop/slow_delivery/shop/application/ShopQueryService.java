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
    public ShopListQueryResult findShopListByCategory(long categoryId, String order, String cursor, int size) {
        if(order.equals("newest"))
            return shopQueryDao.findByCategory(categoryId, cursor, size);
        else if(order.equals("delivery-fee"))
            return shopQueryDao.findByCategoryOrderByDeliveryFee(categoryId, cursor, size);
        else // 컨트롤러단에서 거르고 예외 발생시킬 수 있도록 변경 예정
            throw new IllegalArgumentException();
    }
}
