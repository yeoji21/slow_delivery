package be.shop.slow_delivery.product.infra;

import be.shop.slow_delivery.product.application.dto.ProductDetailInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductQueryDao {
    private final JPAQueryFactory queryFactory;

    public ProductDetailInfo findProductDetailInfo(long productId) {
        return null;
    }
}
