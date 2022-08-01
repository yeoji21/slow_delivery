package be.shop.slow_delivery;

import be.shop.slow_delivery.config.ApplicationAuditingConfig;
import be.shop.slow_delivery.config.JpaQueryFactoryConfig;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.shop.domain.OrderAmountDeliveryFee;
import be.shop.slow_delivery.shop.domain.Shop;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Import({JpaQueryFactoryConfig.class, ApplicationAuditingConfig.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class SlowDeliveryApplicationTests {
	@PersistenceContext
	EntityManager em;

	@Test @Rollback(value = false)
	void test() throws Exception{
		Shop shop = Shop.builder()
				.name("하하하")
				.minOrderAmount(new Money(10_000))
				.deliveryFees(List.of(
						new OrderAmountDeliveryFee(new Money(15_000), new Money(3000)),
						new OrderAmountDeliveryFee(new Money(20_000), new Money(2000))
				))
				.build();

		em.persist(shop);
	}
}
