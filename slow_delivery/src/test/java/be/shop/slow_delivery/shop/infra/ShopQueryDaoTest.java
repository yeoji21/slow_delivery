package be.shop.slow_delivery.shop.infra;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.PhoneNumber;
import be.shop.slow_delivery.config.ApplicationAuditingConfig;
import be.shop.slow_delivery.config.JpaQueryFactoryConfig;
import be.shop.slow_delivery.file.domain.File;
import be.shop.slow_delivery.file.domain.FileName;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import be.shop.slow_delivery.shop.domain.BusinessTimeInfo;
import be.shop.slow_delivery.shop.domain.OrderAmountDeliveryFee;
import be.shop.slow_delivery.shop.domain.Shop;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@Import({JpaQueryFactoryConfig.class, ShopQueryDao.class, ApplicationAuditingConfig.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ShopQueryDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ShopQueryDao shopQueryDao;

    @Test
    void 단건_가게_간략정보_조회() throws Exception{
        //given
        File thumbnailFile = createThumbnailFile();
        Shop shop = createShop();
        shop.updateShopThumbnail(thumbnailFile.getId());

        em.flush();
        em.clear();

        //when
        ShopSimpleInfo info = shopQueryDao.findSimpleInfo(shop.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(info.getShopId()).isEqualTo(shop.getId());
        assertThat(info.getShopName()).isEqualTo(shop.getName());
        assertThat(info.getThumbnailPath()).isEqualTo(thumbnailFile.getFilePath());
        assertThat(info.getMinOrderAmount()).isEqualTo(shop.getMinOrderAmount().toInt());
        assertThat(info.getDefaultDeliveryFees()).hasSize(2);
        info.getDefaultDeliveryFees()
                .forEach(deliveryFee -> assertThat(deliveryFee).isGreaterThan(1000));
    }

    private Shop createShop() {
        Shop shop = Shop.builder()
                .name("A shop")
                .minOrderAmount(new Money(10_000))
                .phoneNumber(new PhoneNumber("111-1111-1111"))
                .businessTimeInfo(new BusinessTimeInfo("", ""))
                .build();
        em.persist(shop);

        em.persist(OrderAmountDeliveryFee.builder()
                .shop(shop)
                .orderAmount(new Money(20_000))
                .fee(new Money(2000))
                .build()
        );
        em.persist(OrderAmountDeliveryFee.builder()
                .shop(shop)
                .orderAmount(new Money(15_000))
                .fee(new Money(3000))
                .build()
        );
        return shop;
    }

    private File createThumbnailFile() {
        File thumbnail = File.builder()
                .fileName(FileName
                        .builder()
                        .originalFileName("original")
                        .storedFileName("stored")
                        .build())
                .filePath("file path")
                .build();
        em.persist(thumbnail);
        return thumbnail;
    }
}