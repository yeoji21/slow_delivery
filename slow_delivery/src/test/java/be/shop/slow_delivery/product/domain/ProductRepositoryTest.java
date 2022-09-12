package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.config.JpaQueryFactoryConfig;
import be.shop.slow_delivery.product.infra.ProductJpaRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Import({JpaQueryFactoryConfig.class, ProductJpaRepository.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductRepositoryTest {
    @PersistenceContext private EntityManager em;
    @Autowired private ProductRepository productRepository;

}




























