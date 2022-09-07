package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.product.application.query.ProductDetailInfo;
import be.shop.slow_delivery.product.infra.ProductQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductQueryService {
    private final ProductQueryDao productQueryDao;

    @Transactional(readOnly = true)
    public ProductDetailInfo findProductDetailInfo(long productId) {
        return productQueryDao.findProductDetailInfo(productId);
    }
}
