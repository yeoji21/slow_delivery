package be.shop.slow_delivery.shop.presentation;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.shop.application.ShopQueryService;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import be.shop.slow_delivery.shop.domain.OrderAmountDeliveryFee;
import be.shop.slow_delivery.shop.domain.Shop;
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
@WebMvcTest(ShopController.class)
@AutoConfigureMockMvc
class ShopControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ShopQueryService shopQueryService;

    @Test
    void 단건_가게_간략정보_조회() throws Exception{
        Shop shop = Shop.builder()
                .name("A shop")
                .minOrderAmount(new Money(10_000))
                .deliveryFees(List.of(new OrderAmountDeliveryFee(new Money(15_000), new Money(3000))))
                .build();

        ShopSimpleInfo shopSimpleInfo =
                new ShopSimpleInfo(1L, shop.getName(), shop.getMinOrderAmount().toInt(),
                        "thumbnail stored path", List.of(3000, 5000));

        given(shopQueryService.findSimpleInfo(any(Long.class))).willReturn(shopSimpleInfo);

        mockMvc.perform(get("/shop/{shopId}/simple", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(shopSimpleInfo)));
    }
}