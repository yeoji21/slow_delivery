package be.shop.slow_delivery.product.presentation;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.ProductCommandService;
import be.shop.slow_delivery.product.application.ProductQueryService;
import be.shop.slow_delivery.product.application.command.ProductCreateCommand;
import be.shop.slow_delivery.product.application.command.ProductValidateCommand;
import be.shop.slow_delivery.product.application.query.*;
import be.shop.slow_delivery.product.presentation.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
class ProductControllerTest {
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ProductQueryService productQueryService;
    @MockBean private ProductCommandService productCommandService;
    @MockBean private ProductDtoMapper mapper;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider contextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(contextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print()).build();
    }

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
        given(mapper.toCreateCommand(any(ProductCreateDto.class))).willReturn(ProductDtoMapper.INSTANCE.toCreateCommand(dto));
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
                .productId(1L)
                .productName("productA")
                .productPrice(15_000)
                .orderQuantity(1)
                .ingredients(getIngredientValidateDtos())
                .options(getOptionValidateDtos())
                .build();
        int totalAmount = 15_000;

        given(mapper.toValidateCommand(any(ProductValidateDto.class))).willReturn(ProductDtoMapper.INSTANCE.toValidateCommand(dto));
        given(productCommandService.validateOrder(any(ProductValidateCommand.class))).willReturn(new Money(totalAmount));

        mockMvc.perform(post("/product/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("validate-product",
                        requestFields(
                                fieldWithPath("productId").type(JsonFieldType.NUMBER).description("주문할 상품의 ID"),
                                fieldWithPath("productName").type(JsonFieldType.STRING).description("주문할 상품의 이름"),
                                fieldWithPath("productPrice").type(JsonFieldType.NUMBER).description("주문할 상품의 가격"),
                                fieldWithPath("orderQuantity").type(JsonFieldType.NUMBER).description("주문 수량"),
                                fieldWithPath("ingredients[].id").type(JsonFieldType.NUMBER).description("필수 옵션 ID 리스트"),
                                fieldWithPath("ingredients[].name").type(JsonFieldType.STRING).description("필수 옵션 ID 리스트"),
                                fieldWithPath("ingredients[].price").type(JsonFieldType.NUMBER).description("필수 옵션 ID 리스트"),
                                fieldWithPath("options[].id").type(JsonFieldType.NUMBER).description("필수 옵션 ID 리스트"),
                                fieldWithPath("options[].name").type(JsonFieldType.STRING).description("필수 옵션 ID 리스트"),
                                fieldWithPath("options[].price").type(JsonFieldType.NUMBER).description("필수 옵션 ID 리스트")
                        )
                ));
    }

    private List<OptionValidateDto> getOptionValidateDtos() {
        OptionValidateDto optionA = OptionValidateDto.builder()
                .id(1L)
                .name("optionA")
                .price(500)
                .build();
        OptionValidateDto optionB = OptionValidateDto.builder()
                .id(5L)
                .name("optionB")
                .price(1000)
                .build();
        return List.of(optionA, optionB);
    }

    private List<IngredientValidateDto> getIngredientValidateDtos() {
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
        return List.of(ingredientA, ingredientB, ingredientC);
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