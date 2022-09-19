package be.shop.slow_delivery.menu.presentation;

import be.shop.slow_delivery.ControllerTest;
import be.shop.slow_delivery.menu.application.dto.request.MenuCreateCommand;
import be.shop.slow_delivery.menu.presentation.dto.MenuFormDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends ControllerTest {
    @Test
    void MENU_생성() throws Exception{

        Long shopId = 1L;

        MenuFormDto menuFormDto = MenuFormDto.builder()
                .menuName("국물")
                .introduction("매움")
                .build();

        MenuCreateCommand menuCreateCommand = menuDtoMapper.toCreateCommand(menuFormDto);

        given(menuService.createMenu(menuCreateCommand,shopId)).willReturn(1L);

        System.out.println(menuFormDto.getMenuName());

        mockMvc.perform(post("/shop/{shopId}/menu/",shopId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuFormDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(1L)));
    }

}