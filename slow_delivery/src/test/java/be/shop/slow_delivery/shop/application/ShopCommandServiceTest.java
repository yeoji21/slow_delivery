package be.shop.slow_delivery.shop.application;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.category.domain.CategoryRepository;
import be.shop.slow_delivery.category.domain.CategoryType;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.shop.application.dto.ShopCommandMapper;
import be.shop.slow_delivery.shop.application.dto.ShopCreateCommand;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopLocation;
import be.shop.slow_delivery.shop.domain.ShopRepository;
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
    @Spy
    private ShopCommandMapper mapper = ShopCommandMapper.INSTANCE;

    @InjectMocks
    private ShopCommandService shopCommandService;

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
        Shop shop = Shop.builder()
                .name("shop A")
                .minOrderAmount(new Money(15_000))
                .phoneNumber("010-1234-5678")
                .description("~~~")
                .openingHours("오후 2시 ~ 익일 새벽 1시")
                .dayOff("연중무휴")
                .location(
                        ShopLocation.builder()
                                .streetAddress("xxx-xxxx")
                                .build())
                .thumbnailFileId(1L)
                .category(new Category(CategoryType.CHICKEN))
                .build();
        boolean before = shop.isOpen();
        long shopId = 1L;
        given(shopRepository.findById(shopId)).willReturn(Optional.ofNullable(shop));

        //when
        shopCommandService.toggleOpenStatus(shopId);
        boolean after = shop.isOpen();

        //then
        assertThat(before).isNotEqualTo(after);
        assertThat(shop.isOpen()).isTrue();

    }
}