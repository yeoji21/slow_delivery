package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.product.application.criteria.ProductCriteriaMapper;
import be.shop.slow_delivery.product.application.criteria.ProductValidateCriteria;
import be.shop.slow_delivery.product.application.query.ProductDetailInfo;
import be.shop.slow_delivery.product.domain.validate.IngredientGroupValidate;
import be.shop.slow_delivery.product.domain.validate.OptionGroupValidate;
import be.shop.slow_delivery.product.domain.validate.ProductValidate;
import be.shop.slow_delivery.product.infra.ProductQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;

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
        ProductValidate product = productQueryDao.findProductValidate(criteria.getId())
                .orElseThrow(IllegalArgumentException::new);
        product.isSatisfy(mapper.toProductValidate(criteria));

        for (IngredientGroupValidate group : productQueryDao
                .findIngredientValidate(criteria.getId(), criteria.getIngredientIdMap())) {
            group.isSatisfy(findOne(mapper.toIngredientValidate(criteria.getIngredientGroups()), g -> g.getId() == group.getId()));
        }

        for (OptionGroupValidate group : productQueryDao
                .findOptionValidate(criteria.getId(), criteria.getOptionIdMap())) {
            group.isSatisfy(findOne(mapper.toOptionValidate(criteria.getOptionGroups()), g -> g.getId() == group.getId()));
        }
    }

    private <T> T findOne(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .findAny()
                .orElseThrow();
    }
}
