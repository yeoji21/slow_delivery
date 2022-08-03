package be.shop.slow_delivery;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.PhoneNumber;
import be.shop.slow_delivery.config.JpaQueryFactoryConfig;
import be.shop.slow_delivery.shop.domain.BusinessTimeInfo;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopLocation;
import be.shop.slow_delivery.shop.infra.ShopQueryDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Import({JpaQueryFactoryConfig.class, ShopQueryDao.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class SlowDeliveryApplicationTests {
	@PersistenceContext
	private EntityManager em;

	@Test @Rollback(value = false)
	void test() throws Exception{
		Shop shop = Shop.builder()
				.name("A shop")
				.minOrderAmount(new Money(10_000))
				.introduction("안녕하세용")
				.phoneNumber(new PhoneNumber("010-1234-1245"))
				.location(ShopLocation.builder()
						.streetAddress("xxx-xxxx")
						.build()
				)
				.businessTimeInfo(new BusinessTimeInfo("매일 10시~21시", "매주 첫째주 일요일"))
				.build();
		em.persist(shop);
	}
}
