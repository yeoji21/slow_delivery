package be.shop.slow_delivery.seller.presentation;

import be.shop.slow_delivery.ControllerTest;
import be.shop.slow_delivery.exception.LoginErrorCode;
import be.shop.slow_delivery.exception.LoginErrorResponse;
import be.shop.slow_delivery.seller.application.dto.*;
import be.shop.slow_delivery.seller.presentation.dto.CheckEmailValidateDto;
import be.shop.slow_delivery.seller.presentation.dto.EmailValidateDto;
import be.shop.slow_delivery.seller.presentation.dto.SellerSignUpDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SellerControllerTest extends ControllerTest {
    @Test @DisplayName("로그인")
    void login() throws Exception {
        //given
        SellerLoginCommand command = SellerLoginCommand.builder()
                .loginId("loginId")
                .password("password")
                .build();
        SellerLoginResult result = new SellerLoginResult("JWT");

        //when
        given(sellerService.login(any(SellerLoginCommand.class))).willReturn(result);

        //then
        mockMvc.perform(post("/seller/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    @Test @DisplayName("회원 가입")
    void signUp() throws Exception{
        //given
        SellerSignUpDto command = SellerSignUpDto.builder()
                .username("username")
                .loginId("loginId")
                .password("password")
                .email("email@test.com")
                .phoneNumber("010-1234-5678")
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

    @Test @DisplayName("본인 인증 메일 전송")
    void sendSignUpValidateEmail() throws Exception{
        //given
        EmailValidateDto dto = new EmailValidateDto("seller@email.com");

        //when
        given(sellerDtoMapper.toCommand(any(EmailValidateDto.class))).willReturn(new EmailValidateCommand(dto.getEmailAddress()));

        //then
        mockMvc.perform(post("/seller/email-validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        verify(sellerService).sendSignUpValidationMail(any(EmailValidateCommand.class));
    }

    @Test @DisplayName("본인 인증 메일로 전송된 코드 검증")
    void verifySignUpValidateEmail() throws Exception{
        //given
        CheckEmailValidateDto dto = CheckEmailValidateDto.builder()
                .emailAddress("test@test.com")
                .code("123456")
                .build();

        //when
        given(sellerDtoMapper.toCriteria(any(CheckEmailValidateDto.class))).willReturn(CheckEmailValidateCriteria.builder().build());

        //then
        mockMvc.perform(get("/seller/email-validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        verify(sellerService).checkSignUpValidationCode(any(CheckEmailValidateCriteria.class));
    }
}