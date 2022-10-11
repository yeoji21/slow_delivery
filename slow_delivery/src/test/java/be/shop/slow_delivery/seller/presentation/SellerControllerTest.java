package be.shop.slow_delivery.seller.presentation;

import be.shop.slow_delivery.ControllerTest;
import be.shop.slow_delivery.exception.LoginErrorCode;
import be.shop.slow_delivery.exception.LoginErrorResponse;
import be.shop.slow_delivery.seller.application.dto.SellerSignUpCommand;
import be.shop.slow_delivery.seller.presentation.dto.SellerSignUpDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SellerControllerTest extends ControllerTest {

    @Test @DisplayName("회원 가입")
    void signUp() throws Exception{
        //given
        SellerSignUpDto command = SellerSignUpDto.builder()
                .username("username")
                .loginId("loginId")
                .password("password")
                .email("email")
                .phoneNumber("phoneNumber")
                .build();
        LoginErrorResponse response = new LoginErrorResponse(LoginErrorCode.SUCCESS);

        //when
        given(sellerDtoMapper.toCommand(any(SellerSignUpDto.class))).willReturn(SellerSignUpCommand.builder().build());
        given(sellerService.signUp(any(SellerSignUpCommand.class))).willReturn(LoginErrorCode.SUCCESS);

        //then
        mockMvc.perform(post("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

    }
}