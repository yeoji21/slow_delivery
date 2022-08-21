package be.shop.slow_delivery.product.infra;

import be.shop.slow_delivery.product.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ProductJpaRepository implements ProductRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public Map<IngredientGroup, List<Ingredient>> findIngredientsMap(long productId, List<Long> ingredientIds) {
        return null;
    }

    @Override
    public Map<OptionGroup, List<Option>> findOptionsMap(long productId, List<Long> optionIds) {
        return null;
    }
}








































