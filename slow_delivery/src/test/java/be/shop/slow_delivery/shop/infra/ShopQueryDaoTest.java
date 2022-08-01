package be.shop.slow_delivery.shop.infra;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.config.JpaQueryFactoryConfig;
import be.shop.slow_delivery.file.domain.File;
import be.shop.slow_delivery.file.domain.FileName;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({JpaQueryFactoryConfig.class, ShopQueryDao.class})
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
        shop.setShopThumbnail(thumbnailFile.getId());

        //when
        ShopSimpleInfo info = shopQueryDao.findSimpleInfo(shop.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(info.getShopId()).isEqualTo(shop.getId());
        assertThat(info.getShopName()).isEqualTo(shop.getName());
        assertThat(info.getThumbnail()).isEqualTo(thumbnailFile.getFilePath());
        assertThat(info.getMinOrderAmount()).isEqualTo(shop.getMinOrderAmount().toInt());
        assertThat(info.getDefaultDeliveryFees()).hasSize(2);
        info.getDefaultDeliveryFees()
                .forEach(deliveryFee -> assertThat(deliveryFee).isGreaterThan(0));
    }

    private Shop createShop() {
        Shop shop = Shop.builder()
                .name("A shop")
                .minOrderAmount(new Money(10_000))
                .deliveryFees(List.of(
                        OrderAmountDeliveryFee.builder()
                                .orderAmount(new Money(20_000))
                                .fee(new Money(2000))
                                .build(),
                        OrderAmountDeliveryFee.builder()
                                .orderAmount(new Money(15_000))
                                .fee(new Money(3000))
                                .build()))
                .build();

        em.persist(shop);
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