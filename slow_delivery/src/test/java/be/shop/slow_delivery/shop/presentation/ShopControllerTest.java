package be.shop.slow_delivery.shop.presentation;

import be.shop.slow_delivery.ControllerTest;
import be.shop.slow_delivery.shop.application.dto.ShopDetailInfo;
import be.shop.slow_delivery.shop.application.dto.ShopListQueryResult;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import be.shop.slow_delivery.shop.presentation.dto.ShopCreateDto;
import be.shop.slow_delivery.shop.presentation.dto.ShopDtoMapper;
import be.shop.slow_delivery.shop.presentation.dto.ShopOrderType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ShopControllerTest extends ControllerTest {
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

        given(shopDtoMapper.toCreateCommand(any(ShopCreateDto.class)))
                .willReturn(ShopDtoMapper.INSTANCE.toCreateCommand(shopCreateDto));
        given(shopCommandService.create(any())).willReturn(1L);

        mockMvc.perform(post("/shop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopCreateDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(1L)));
    }

    @Test
    void 영업_상태_변경() throws Exception{
        mockMvc.perform(patch("/shop/{shopId}/open", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(shopCommandService).toggleOpenStatus(1L);
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
        ShopDetailInfo shopDetailInfo = ShopDetailInfo.builder()
                .shopId(1L)
                .shopName("A shop")
                .thumbnailPath("thumbnail path")
                .minOrderAmount(10_000)
                .phoneNumber("010-1234-5678")
                .openingHours("매일 15시 ~ 02시")
                .dayOff("연중무휴")
                .streetAddress("xxxx-xxxx")
                .defaultDeliveryFees(List.of(3000, 2000))
                .build();

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
        ShopOrderType order = ShopOrderType.NEWEST;

        given(shopQueryService.findShopsOrderByNewest(1L, null, 10)).willReturn(result);

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