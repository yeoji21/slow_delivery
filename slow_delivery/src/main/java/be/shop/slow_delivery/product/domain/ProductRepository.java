package be.shop.slow_delivery.product.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(long productId);
    Map<IngredientGroup, List<Ingredient>> findIngredientsMap(long productId, List<Long> ingredientIds);
    Map<OptionGroup, List<Option>> findOptionsMap(long productId, List<Long> optionIds);
}
