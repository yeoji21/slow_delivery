package be.shop.slow_delivery.product.application;

import be.shop.slow_delivery.product.application.command.ProductValidateCommand;
import be.shop.slow_delivery.product.application.query.ProductDetailInfo;
import be.shop.slow_delivery.product.domain.validate.IngredientGroupValidate;
import be.shop.slow_delivery.product.domain.validate.ProductValidate;
import be.shop.slow_delivery.product.infra.ProductQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductQueryService {
    private final ProductQueryDao productQueryDao;

    @Transactional(readOnly = true)
    public ProductDetailInfo findProductDetailInfo(long productId) {
        return productQueryDao.findProductDetailInfo(productId);
    }

    @Transactional(readOnly = true)
    public void validateOrder(ProductValidateCommand command) {
        ProductValidate product = productQueryDao.findProductValidate(command.getId())
                .orElseThrow(IllegalArgumentException::new);
        product.isEqualTo(command.toProductValidate());

        List<IngredientGroupValidate> ingredientGroups = productQueryDao
                .findIngredientValidate(command.getId(), command.getIngredientIdMap());
        for (IngredientGroupValidate group : command.toIngredientGroupValidate()) {
            IngredientGroupValidate findOne = ingredientGroups
                    .stream()
                    .filter(r -> r.getId() == group.getId())
                    .findAny()
                    .orElseThrow();
            group.isEqualTo(findOne);
        }

    }

}
