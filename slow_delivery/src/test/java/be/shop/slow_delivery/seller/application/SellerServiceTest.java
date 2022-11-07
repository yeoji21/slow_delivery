package be.shop.slow_delivery.seller.application;

import be.shop.slow_delivery.exception.InvalidValueException;
import be.shop.slow_delivery.jwt.TokenProvider;
import be.shop.slow_delivery.seller.application.dto.*;
import be.shop.slow_delivery.seller.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {
    @Mock private SellerRepository sellerRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private TokenProvider tokenProvider;
    @Mock private EmailSender emailSender;
    @Mock private EmailMessageFactory emailMessageFactory;
    @Mock private SecretCodeService secretCodeService;
    @Mock private SecretCodeFactory secretCodeFactory;
    @InjectMocks private SellerService sellerService;

    @Test @DisplayName("로그인 성공")
    void loginSuccess() throws Exception{
        //given
        Seller seller = Seller.builder()
                .username("username")
                .loginId("loginId")
                .password("password")
                .email("email@test.com")
                .phoneNumber("010-1234-5678")
                .role(SellerRole.USER)
                .build();
        SellerLoginCommand command = SellerLoginCommand.builder()
                .loginId("id")
                .password("password")
                .build();
        String jwt = "JWT";

        given(sellerRepository.findByLoginId(anyString())).willReturn(Optional.ofNullable(seller));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(tokenProvider.generateToken(any(Seller.class))).willReturn(jwt);

        //when
        SellerLoginResult result = sellerService.login(command);

        //then
        assertThat(result.getToken()).isEqualTo(jwt);
    }

    @Test @DisplayName("비밀번호가 달라 로그인 실패")
    void loginFail() throws Exception {
        //given
        SellerLoginCommand command = SellerLoginCommand.builder()
                .loginId("id")
                .password("password")
                .build();
        Seller seller = Seller.builder()
                .username("username")
                .loginId("loginId")
                .password("password")
                .email("email@test.com")
                .phoneNumber("010-1234-5678")
                .role(SellerRole.USER)
                .build();
        given(sellerRepository.findByLoginId(anyString())).willReturn(Optional.ofNullable(seller));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> sellerService.login(command));
    }


    @Test @DisplayName("회원 가입")
    void signUp() throws Exception{
        //given
        SellerSignUpCommand command = SellerSignUpCommand.builder()
                .username("username")
                .loginId("loginId")
                .password("password")
                .email("email@test.com")
                .phoneNumber("010-1234-5678")
                .build();

        given(sellerRepository.findByLoginId(anyString())).willReturn(Optional.empty());

        //when
        sellerService.signUp(command);

        //then
        verify(sellerRepository).save(any(Seller.class));
    }


    @Test @DisplayName("가입 시 이메일 검증")
    void sendSignUpValidationMail() throws Exception{
        //given
        EmailValidateCommand command = new EmailValidateCommand("seller@email.com");
        given(sellerRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(secretCodeFactory.generate()).willReturn("secret code");
        given(emailMessageFactory.emailValidateMessage(anyString(), anyString()))
                .willReturn(EmailMessage.builder().build());

        //when
        sellerService.sendSignUpValidationMail(command);

        //then
        verify(emailSender).send(any(EmailMessage.class));
        verify(secretCodeService).saveSignUpCode(anyString(), anyString());
    }

    @Test @DisplayName("가입 이메일 코드 검증 성공")
    void checkSignUpValidationCode() throws Exception{
        //given
        CheckEmailValidateCriteria criteria = CheckEmailValidateCriteria.builder()
                .emailAddress("test@test.com")
                .code("123456")
                .build();
        given(secretCodeService.findSighUpCode(anyString())).willReturn(criteria.getCode());

        //when
        sellerService.checkSignUpValidationCode(criteria);

        //then
    }

    @Test @DisplayName("가입 이메일 코드 검증 실패")
    void checkSignUpValidationCode_exception2() throws Exception{
        //given
        CheckEmailValidateCriteria criteria = CheckEmailValidateCriteria.builder()
                .emailAddress("test@test.com")
                .code("123456")
                .build();
        given(secretCodeService.findSighUpCode(anyString())).willReturn("xxxxxxx");

        //when

        //then
        assertThrows(InvalidValueException.class, () -> sellerService.checkSignUpValidationCode(criteria));
    }
}