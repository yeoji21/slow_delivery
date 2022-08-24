package be.shop.slow_delivery.product.presentation;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.ProductQueryService;
import be.shop.slow_delivery.product.application.dto.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ProductQueryService productQueryService;

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
        List<OptionInfo> options = List.of(new OptionInfo(1L, "optionA", 500),
                new OptionInfo(2L, "optionB", 1000),
                new OptionInfo(3L, "optionC", 1500));

        return OptionGroupInfo.builder()
                .optionGroupId(1L)
                .maxSelectCount(5)
                .name("optionGroupA")
                .options(options)
                .build();
    }

    private IngredientGroupInfo getIngredientGroupInfo() {
        List<IngredientInfo> ingredients = List.of(new IngredientInfo(1L, "ingredientA", 1000),
                        new IngredientInfo(2L, "ingredientB", 2000),
                        new IngredientInfo(3L, "ingredientC", 3000));

        return IngredientGroupInfo.builder()
                .name("ingredientGroupA")
                .ingredientGroupId(1L)
                .minSelectCount(1)
                .maxSelectCount(2)
                .ingredients(ingredients)
                .build();
    }
}