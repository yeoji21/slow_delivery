package be.shop.slow_delivery.shop.presentation;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.PhoneNumber;
import be.shop.slow_delivery.shop.application.ShopCommandService;
import be.shop.slow_delivery.shop.application.ShopQueryService;
import be.shop.slow_delivery.shop.application.dto.ShopCreateCommand;
import be.shop.slow_delivery.shop.application.dto.ShopDetailInfo;
import be.shop.slow_delivery.shop.application.dto.ShopListQueryResult;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import be.shop.slow_delivery.shop.domain.BusinessTimeInfo;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopLocation;
import be.shop.slow_delivery.shop.presentation.dto.ShopCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ShopController.class)
@AutoConfigureMockMvc
class ShopControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ShopQueryService shopQueryService;
    @MockBean private ShopCommandService shopCommandService;

    @Test
    void 가게_생성() throws Exception{
        ShopCreateDto shopCreateDto = ShopCreateDto.builder()
                .shopName("가게 A")
                .introduction("안녕하세요~!")
                .phoneNumber("010-1234-5678")
                .streetAddress("xxx시 yy구 zzz동 123-456")
                .openingHours("오후 4시 ~ 익일 새벽 2시")
                .dayOff("연중무휴")
                .minOrderAmount(15_000)
                .category("치킨")
                .build();

        given(shopCommandService.create(any(ShopCreateCommand.class))).willReturn(1L);

        mockMvc.perform(post("/shop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopCreateDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(1L)));
    }


    @Test
    void 단건_가게_간략정보_조회() throws Exception{
        ShopSimpleInfo shopSimpleInfo =
                ShopSimpleInfo.builder()
                        .shopId(1L)
                        .shopName("A shop")
                        .minOrderAmount(15_000)
                        .thumbnailPath("thumbnail stored path")
                        .defaultDeliveryFees(List.of(3000, 2000))
                        .build();

        given(shopQueryService.findSimpleInfo(any(Long.class))).willReturn(shopSimpleInfo);

        mockMvc.perform(get("/shop/{shopId}/simple", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(shopSimpleInfo)));
    }

    @Test
    void 단건_가게_상세정보_조회() throws Exception{
        Shop shop = Shop.builder()
                .id(1L)
                .name("A shop")
                .minOrderAmount(new Money(10_000))
                .phoneNumber(new PhoneNumber("010-1234-5678"))
                .businessTimeInfo(new BusinessTimeInfo("매일 15시 ~ 02시", "연중무휴"))
                .location(ShopLocation.builder().streetAddress("xxxx-xxxx").build())
                .category(new Category("음식"))
                .build();

        ShopDetailInfo shopDetailInfo = new ShopDetailInfo(shop, "thumbnail path", List.of(3000, 2000));

        given(shopQueryService.findDetailInfo(any(Long.class))).willReturn(shopDetailInfo);

        mockMvc.perform(get("/shop/{shopId}/detail", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(shopDetailInfo)));
    }

    @Test
    void 카테고리별_가게_목록_조회() throws Exception{
        List<ShopSimpleInfo> shopList = getShopSimpleInfoList();
        ShopListQueryResult result = new ShopListQueryResult(shopList, true, "nextCursor");
        String order = "newest";

        given(shopQueryService.findShopListByCategory(1L, order, null, 10)).willReturn(result);

        mockMvc.perform(get("/category/{categoryId}/shop?order={order}", 1L, order)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    private List<ShopSimpleInfo> getShopSimpleInfoList() {
        ShopSimpleInfo AShopInfo = ShopSimpleInfo.builder()
                .shopId(1L)
                .shopName("A shop")
                .minOrderAmount(15_000)
                .thumbnailPath("thumbnail stored path")
                .defaultDeliveryFees(List.of(3000, 2000))
                .build();
        ShopSimpleInfo BShopInfo = ShopSimpleInfo.builder()
                .shopId(2L)
                .shopName("B shop")
                .minOrderAmount(10_000)
                .thumbnailPath("thumbnail stored path")
                .defaultDeliveryFees(List.of(2000, 1000))
                .build();

        return List.of(AShopInfo, BShopInfo);
    }
}