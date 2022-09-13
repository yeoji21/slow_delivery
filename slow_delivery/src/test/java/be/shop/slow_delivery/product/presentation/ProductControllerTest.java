package be.shop.slow_delivery.product.presentation;

import be.shop.slow_delivery.ControllerTest;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.command.ProductCreateCommand;
import be.shop.slow_delivery.product.application.criteria.ProductValidateCriteria;
import be.shop.slow_delivery.product.application.query.*;
import be.shop.slow_delivery.product.presentation.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends ControllerTest {
    @Test
    void 상품_생성() throws Exception{
        ProductCreateDto dto = ProductCreateDto.builder()
                .name("product")
                .description("~~~")
                .price(10_000)
                .maxOrderQuantity(3)
                .stock(500)
                .build();

        long productId = 1L;
        given(productDtoMapper.toCreateCommand(any(ProductCreateDto.class)))
                .willReturn(ProductDtoMapper.INSTANCE.toCreateCommand(dto));
        given(productCommandService.create(any(ProductCreateCommand.class))).willReturn(productId);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productId)))
                .andDo(document("create-product",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("상품 소개"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("maxOrderQuantity").type(JsonFieldType.NUMBER).description("최대 주문 수량"),
                                fieldWithPath("stock").type(JsonFieldType.NUMBER).description("초기 재고"))
                ));
    }

    @Test
    void 상품_주문_검증() throws Exception{
        ProductValidateDto dto = ProductValidateDto.builder()
                .id(1L)
                .name("productA")
                .price(15_000)
                .orderQuantity(1)
                .ingredientGroups(getIngredientGroups())
                .optionGroups(getOptionGroups())
                .build();

        given(productDtoMapper.toValidateCommand(any(ProductValidateDto.class)))
                .willReturn(ProductDtoMapper.INSTANCE.toValidateCommand(dto));

        mockMvc.perform(post("/product/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("validate-product",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("orderQuantity").type(JsonFieldType.NUMBER).description("주문 수량"),
                                fieldWithPath("ingredientGroups[].id").type(JsonFieldType.NUMBER).description("필수 옵션 그룹 ID"),
                                fieldWithPath("ingredientGroups[].name").type(JsonFieldType.STRING).description("필수 옵션 그룹명"),
                                fieldWithPath("ingredientGroups[].ingredients[].id").type(JsonFieldType.NUMBER).description("필수 옵션 ID"),
                                fieldWithPath("ingredientGroups[].ingredients[].name").type(JsonFieldType.STRING).description("필수 옵션명"),
                                fieldWithPath("ingredientGroups[].ingredients[].price").type(JsonFieldType.NUMBER).description("필수 옵션 가격"),
                                fieldWithPath("optionGroups[].id").type(JsonFieldType.NUMBER).description("선택 옵션 그룹 ID"),
                                fieldWithPath("optionGroups[].name").type(JsonFieldType.STRING).description("선택 옵션 그룹명"),
                                fieldWithPath("optionGroups[].options[].id").type(JsonFieldType.NUMBER).description("선택 옵션 ID"),
                                fieldWithPath("optionGroups[].options[].name").type(JsonFieldType.STRING).description("선택 옵션명"),
                                fieldWithPath("optionGroups[].options[].price").type(JsonFieldType.NUMBER).description("선택 옵션 가격")
                        )
                ));

        verify(productQueryService).validateOrder(any(ProductValidateCriteria.class));
    }

    private List<OptionGroupValidateDto> getOptionGroups() {
        OptionGroupValidateDto optionGroupA = OptionGroupValidateDto.builder()
                .id(1L)
                .name("O-groupA")
                .options(List.of(OptionValidateDto.builder()
                                .id(1L)
                                .name("optionA")
                                .price(500)
                                .build(),
                        OptionValidateDto.builder()
                                .id(5L)
                                .name("optionB")
                                .price(1000)
                                .build()))
                .build();
        List<OptionGroupValidateDto> optionGroups = List.of(optionGroupA);
        return optionGroups;
    }

    private List<IngredientGroupValidateDto> getIngredientGroups() {
        IngredientValidateDto ingredientA = IngredientValidateDto.builder()
                .id(1L)
                .name("ingredientA")
                .price(1000)
                .build();
        IngredientValidateDto ingredientB = IngredientValidateDto.builder()
                .id(2L)
                .name("ingredientB")
                .price(2000)
                .build();
        IngredientValidateDto ingredientC = IngredientValidateDto.builder()
                .id(3L)
                .name("ingredientC")
                .price(3000)
                .build();

        IngredientGroupValidateDto groupA = IngredientGroupValidateDto.builder()
                .id(1L)
                .name("I-groupA")
                .ingredients(List.of(ingredientA, ingredientB))
                .build();

        IngredientGroupValidateDto groupB = IngredientGroupValidateDto.builder()
                .id(2L)
                .name("I-groupB")
                .ingredients(List.of(ingredientA, ingredientC))
                .build();
        List<IngredientGroupValidateDto> groups = List.of(groupA, groupB);
        return groups;
    }

    @Test
    void 모든_옵션_포함_상품_조회() throws Exception{
        ProductDetailInfo productDetailInfo = ProductDetailInfo.builder()
                .productId(1L)
                .name("productA")
                .description("~~~")
                .price(new Money(15_000))
                .maxOrderQuantity(new Quantity(5))
                .thumbnailFileId(1L)
                .build();
        productDetailInfo.setIngredientGroups(List.of(getIngredientGroupInfo()));
        productDetailInfo.setOptionGroups(List.of(getOptionGroupInfo()));

        given(productQueryService.findProductDetailInfo(any(Long.class))).willReturn(productDetailInfo);

        mockMvc.perform(get("/product/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productDetailInfo)));
    }

    private OptionGroupInfo getOptionGroupInfo() {
        List<OptionInfo> options = List.of(new OptionInfo(1L, "optionA", new Money(500)),
                new OptionInfo(2L, "optionB", new Money(1000)),
                new OptionInfo(3L, "optionC", new Money(1500)));

        return OptionGroupInfo.builder()
                .optionGroupId(1L)
                .maxSelectCount(new Quantity(5))
                .name("optionGroupA")
                .options(options)
                .build();
    }

    private IngredientGroupInfo getIngredientGroupInfo() {
        List<IngredientInfo> ingredients = List.of(new IngredientInfo(1L, "ingredientA", new Money(1000)),
                        new IngredientInfo(2L, "ingredientB", new Money(2000)),
                        new IngredientInfo(3L, "ingredientC", new Money(3000)));

        return IngredientGroupInfo.builder()
                .name("ingredientGroupA")
                .ingredientGroupId(1L)
                .minSelectCount(new Quantity(1))
                .maxSelectCount(new Quantity(2))
                .ingredients(ingredients)
                .build();
    }
}