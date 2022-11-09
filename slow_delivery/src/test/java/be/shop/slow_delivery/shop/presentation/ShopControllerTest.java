package be.shop.slow_delivery.shop.presentation;

import be.shop.slow_delivery.ControllerTest;
import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.category.domain.CategoryType;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.shop.application.dto.*;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopLocation;
import be.shop.slow_delivery.shop.presentation.dto.ShopCreateDto;
import be.shop.slow_delivery.shop.presentation.dto.ShopDtoMapper;
import be.shop.slow_delivery.shop.presentation.dto.ShopInfoModifyDto;
import be.shop.slow_delivery.shop.presentation.dto.ShopOrderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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

        given(shopDtoMapper.toCommand(any(ShopCreateDto.class)))
                .willReturn(ShopDtoMapper.INSTANCE.toCommand(shopCreateDto));
        given(shopCommandService.create(any())).willReturn(1L);

        mockMvc.perform(post("/shop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopCreateDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(1L)));
    }

    @Test @DisplayName("가게 기본 정보 수정")
    void updateInfo() throws Exception{
        //given
        ShopInfoModifyDto dto = ShopInfoModifyDto.builder()
                .minOrderPrice(10_000)
                .description("가게 소개글")
                .openingHours("영업시간")
                .dayOff("휴무일")
                .build();

        //when
        given(shopDtoMapper.toCommand(any(ShopInfoModifyDto.class))).willReturn(ShopInfoModifyCommand.builder().build());

        //then
        mockMvc.perform(patch("/shop/{shopId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        verify(shopCommandService).update(any(Long.class), any(ShopInfoModifyCommand.class));
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
        Shop shop = Shop.builder()
                .name("shop A")
                .minOrderAmount(new Money(15_000))
                .phoneNumber("010-1234-5678")
                .description("~~~")
                .openingHours("오후 2시 ~ 익일 새벽 1시")
                .dayOff("연중무휴")
                .location(ShopLocation.builder()
                        .areaId(1L)
                        .streetAddress("xxx-xxxx")
                        .build())
                .thumbnailFileId(1L)
                .category(new Category(CategoryType.CHICKEN))
                .build();
        ReflectionTestUtils.setField(shop, "id", 1L);
        ShopDetailInfo shopDetailInfo = new ShopDetailInfo(shop,
                "thumbnail file path",
                List.of(new DeliveryFeeInfo(10_000, 3000),
                        new DeliveryFeeInfo(15_000, 2000),
                        new DeliveryFeeInfo(20_000, 1000)),
                List.of("category1", "category2"));

        given(shopQueryService.findDetailInfo(any(Long.class))).willReturn(shopDetailInfo);

        mockMvc.perform(get("/shop/{shopId}/detail", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(shopDetailInfo)))
                .andDo(document("shop-detail-info",
                        responseFields(
                                fieldWithPath("shopId").description("가게 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("thumbnailPath").description("가게 썸네일 이미지 저장 경로").type(JsonFieldType.STRING),
                                fieldWithPath("areaId").description("가게 권역 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("shopName").description("가게명").type(JsonFieldType.STRING),
                                fieldWithPath("minOrderAmount").description("최소 주문 금액").type(JsonFieldType.NUMBER),
                                fieldWithPath("phoneNumber").description("가게 번호").type(JsonFieldType.STRING),
                                fieldWithPath("description").description("소개글").type(JsonFieldType.STRING),
                                fieldWithPath("openingHours").description("영업 시간").type(JsonFieldType.STRING),
                                fieldWithPath("dayOff").description("휴무일").type(JsonFieldType.STRING),
                                fieldWithPath("streetAddress").description("도로명 주소").type(JsonFieldType.STRING),
                                fieldWithPath("openStatus").description("영업 여부 : true - 영업 중, false - 준비 중").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("deliveryFees[].orderAmount").description("주문 금액 별 배달비에서 주문 금액").type(JsonFieldType.NUMBER),
                                fieldWithPath("deliveryFees[].deliveryFee").description("주문 금액 별 배달비에서 배달비").type(JsonFieldType.NUMBER),
                                fieldWithPath("categories").description("가게 카테고리").type(JsonFieldType.ARRAY)
                        )
                ));
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