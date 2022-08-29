package be.shop.slow_delivery.menu.presentation;

import be.shop.slow_delivery.menu.application.MenuDisplayService;
import be.shop.slow_delivery.menu.application.MenuService;
import be.shop.slow_delivery.menu.application.dto.request.MenuCreateRequestDto;
import be.shop.slow_delivery.menu.presentation.dto.MenuDtoMapper;
import be.shop.slow_delivery.menu.presentation.dto.MenuFormDto;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MenuController.class)
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private MenuService menuService;
    @MockBean private MenuDtoMapper mapper;
    @MockBean private MenuDisplayService menuDisplayService;

    @Test
    void MENU_생성() throws Exception{

        Long shopId = 1L;

        MenuFormDto menuFormDto = MenuFormDto.builder()
                .menuName("국물")
                .introduction("매움")
                .build();

        MenuCreateRequestDto menuCreateRequestDto = mapper.toCreateRequestDto(menuFormDto);

        given(menuService.createMenu(menuCreateRequestDto,shopId)).willReturn(1L);

        System.out.println(menuFormDto.getMenuName());

        mockMvc.perform(post("/shop/{shopId}/menu/",shopId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuFormDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(1L)));
    }

}