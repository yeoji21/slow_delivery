package be.shop.slow_delivery.shop.application;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.category.domain.CategoryRepository;
import be.shop.slow_delivery.category.domain.CategoryType;
import be.shop.slow_delivery.common.client.BeServiceClient;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.shop.application.dto.ShopCommandMapper;
import be.shop.slow_delivery.shop.application.dto.ShopCreateCommand;
import be.shop.slow_delivery.shop.application.dto.ShopDetailInfo;
import be.shop.slow_delivery.shop.application.dto.ShopInfoModifyCommand;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopLocation;
import be.shop.slow_delivery.shop.domain.ShopRepository;
import be.shop.slow_delivery.shop.infra.ShopQueryDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ShopCommandServiceTest {
    @Mock
    private ShopRepository shopRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BeServiceClient serviceClient;
    @Mock
    private ShopQueryDao shopQueryDao;
    @Spy
    private ShopCommandMapper mapper = ShopCommandMapper.INSTANCE;

    @InjectMocks
    private ShopCommandService shopCommandService;

    @Test @DisplayName("가게 정보 수정")
    void update() throws Exception{
        //given
        ShopInfoModifyCommand command = ShopInfoModifyCommand.builder()
                .minOrderAmount(10_000)
                .description("변경된 가게 소개글")
                .openingHours("변경된 영업시간")
                .dayOff("변경된 휴무일")
                .build();
        Shop shop = createShop();
        given(shopRepository.findById(1L)).willReturn(Optional.ofNullable(shop));

        //when
        shopCommandService.update(1L, command);

        //then
        assertThat(shop.getDescription()).startsWith("변경된");
        assertThat(shop.getBusinessTimeInfo().getOpeningHours()).startsWith("변경된");
        assertThat(shop.getBusinessTimeInfo().getDayOff()).startsWith("변경된");
        assertThat(shop.getMinOrderAmount().toInt()).isEqualTo(10_000);
    }

    @Test
    void 가게_생성() throws Exception{
        //given
        ShopCreateCommand command = ShopCreateCommand.builder()
                .shopName("가게 A")
                .introduction("안녕하세요~!")
                .phoneNumber("010-1234-5678")
                .streetAddress("xxx시 yy구 zzz동 123-456")
                .openingHours("오후 4시 ~ 익일 새벽 2시")
                .dayOff("연중무휴")
                .minOrderAmount(15_000)
                .category("치킨")
                .build();
        Category category = new Category(CategoryType.CHICKEN);
        ReflectionTestUtils.setField(category, "id", 1L);

        given(categoryRepository.findByCategoryName(any(String.class))).willReturn(Optional.of(category));
        doAnswer(invocation -> {
            Shop toSave = (Shop) invocation.getArgument(0);
            ReflectionTestUtils.setField(toSave, "id", 1L);
            return toSave;
        }).when(shopRepository).save(any(Shop.class));

        //when
        long shopId = shopCommandService.create(command);

        //then
        assertThat(shopId).isGreaterThan(0L);
        verify(mapper).toShop(any(ShopCreateCommand.class), any(Category.class));
    }

    @Test
    void 영업_상태_변경() throws Exception{
        //given
        Shop shop = createShop();
        ReflectionTestUtils.setField(shop, "id", 1L);
        boolean before = shop.isOpen();
        long shopId = 1L;
        ShopDetailInfo info = ShopDetailInfo.builder()
                .shop(shop)
                .build();
        given(shopRepository.findById(shopId)).willReturn(Optional.ofNullable(shop));
        given(shopQueryDao.findDetailInfo(shopId)).willReturn(Optional.ofNullable(info));

        //when
        shopCommandService.toggleOpenStatus(shopId);
        boolean after = shop.isOpen();

        //then
        assertThat(before).isNotEqualTo(after);
        assertThat(shop.isOpen()).isTrue();
        verify(serviceClient).updateShopInfo(any(Long.class), any(ShopDetailInfo.class));
    }

    private Shop createShop() {
        Shop shop = Shop.builder()
                .name("shop A")
                .minOrderAmount(new Money(15_000))
                .phoneNumber("010-1234-5678")
                .description("~~~")
                .openingHours("오후 2시 ~ 익일 새벽 1시")
                .dayOff("연중무휴")
                .location(ShopLocation.builder()
                                .streetAddress("xxx-xxxx")
                                .build())
                .thumbnailFileId(1L)
                .category(new Category(CategoryType.CHICKEN))
                .build();
        return shop;
    }
}