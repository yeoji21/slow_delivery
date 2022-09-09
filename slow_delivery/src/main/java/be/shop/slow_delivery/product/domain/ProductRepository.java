package be.shop.slow_delivery.product.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(long productId);
    Map<IngredientGroup, List<Ingredient>> findIngredientMap(long productId, List<Long> ingredientIds);
    Map<OptionGroup, List<Option>> findOptionMap(long productId, List<Long> optionIds);
    Map<IngredientGroup, List<Ingredient>> findIngredientMap(long productId, Map<Long, List<Long>> ingredientIdMap);
    Map<OptionGroup, List<Option>> findOptionMap(Long productId, Map<Long, List<Long>> optionIdMap);
}
