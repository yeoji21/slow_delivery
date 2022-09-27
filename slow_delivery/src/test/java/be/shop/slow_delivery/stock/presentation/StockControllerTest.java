package be.shop.slow_delivery.stock.presentation;

import be.shop.slow_delivery.ControllerTest;
import be.shop.slow_delivery.stock.presentation.dto.StockDto;
import be.shop.slow_delivery.stock.presentation.dto.StockReduceDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StockControllerTest extends ControllerTest {

    @Test
    void 재고_차감_테스트() throws Exception{
        //given
        List<StockDto> dtos = new ArrayList<>();
        dtos.add(new StockDto(1L, 5));
        dtos.add(new StockDto(2L, 1));
        dtos.add(new StockDto(3L, 1));
        StockReduceDto dto = new StockReduceDto(dtos);

        //when

        //then
        mockMvc.perform(patch("/stock/reduce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("stock-reduceByDBLock",
                        requestFields(
                                fieldWithPath("stocks[].stockId").description("stockId"),
                                fieldWithPath("stocks[].quantity").description("quantity")
                        )
                ));
    }
}