package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.product.application.criteria.ProductCriteriaMapper;
import be.shop.slow_delivery.product.application.criteria.ProductValidateCriteria;
import be.shop.slow_delivery.product.application.query.ProductDetailInfo;
import be.shop.slow_delivery.product.domain.validate.ProductValidate;
import be.shop.slow_delivery.product.infra.ProductQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductQueryService {
    private final ProductQueryDao productQueryDao;
    private final ProductCriteriaMapper mapper;

    @Transactional(readOnly = true)
    public ProductDetailInfo findProductDetailInfo(long productId) {
        return productQueryDao.findProductDetailInfo(productId);
    }

    @Transactional(readOnly = true)
    public void validateOrder(ProductValidateCriteria criteria) {
        ProductValidate product = productQueryDao.findProductValidate(criteria.getId(),
                criteria.getIngredientIdMap(), criteria.getOptionIdMap());
        product.isSatisfy(mapper.toProductValidate(criteria));
    }
}
