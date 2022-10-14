package be.shop.slow_delivery.seller.application;

import be.shop.slow_delivery.jwt.TokenProvider;
import be.shop.slow_delivery.seller.application.dto.EmailMessage;
import be.shop.slow_delivery.seller.application.dto.EmailValidateCommand;
import be.shop.slow_delivery.seller.domain.EmailMessageGenerator;
import be.shop.slow_delivery.seller.domain.EmailSender;
import be.shop.slow_delivery.seller.domain.SecretCodeService;
import be.shop.slow_delivery.seller.domain.SellerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {
    @Mock private SellerRepository sellerRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock private TokenProvider tokenProvider;
    @Mock private EmailSender emailSender;
    @Mock private EmailMessageGenerator emailMessageGenerator;
    @Mock private SecretCodeService secretCodeService;
    @InjectMocks private SellerService sellerService;

    @Test @DisplayName("가입 시 이메일 검증")
    void emailValidate() throws Exception{
        //given
        EmailValidateCommand command = new EmailValidateCommand("seller@email.com");
        given(sellerRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(secretCodeService.generateEmailValidateCode(anyString())).willReturn("secret code");
        given(emailMessageGenerator.emailValidateMessage(anyString(), anyString()))
                .willReturn(EmailMessage.builder().build());

        //when
        sellerService.emailValidate(command);

        //then
        verify(emailSender).send(any(EmailMessage.class));
    }
}