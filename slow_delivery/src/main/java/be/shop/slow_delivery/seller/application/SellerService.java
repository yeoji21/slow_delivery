package be.shop.slow_delivery.seller.application;

import be.shop.slow_delivery.exception.ErrorCode;
import be.shop.slow_delivery.exception.InvalidValueException;
import be.shop.slow_delivery.exception.LoginErrorCode;
import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.jwt.TokenProvider;
import be.shop.slow_delivery.seller.application.dto.*;
import be.shop.slow_delivery.seller.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailSender emailSender;
    private final SecretCodeService secretCodeService;
    private final EmailMessageFactory emailMessageFactory;
    private final SecretCodeFactory secretCodeFactory;

    @Transactional
    public SellerLoginResult login(SellerLoginCommand sellerLoginCommand){
        Seller seller = sellerRepository.findByLoginId(sellerLoginCommand.getLoginId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SELLER_NOT_FOUND));
        if(!passwordEncoder.matches(sellerLoginCommand.getPassword(), seller.getPassword()))
            throw new IllegalArgumentException("아이디와 비밀번호가 일치하지 않습니다.");
        return new SellerLoginResult(tokenProvider.generateToken(seller));
    }

    @Transactional
    public LoginErrorCode signUp(SellerSignUpCommand command) {
        if(findSellerById(command.getLoginId()).isPresent())
            return LoginErrorCode.DUPLICATE_EMAIL;

        Seller seller = Seller.builder()
                .loginId(command.getLoginId())
                .password(passwordEncoder.encode(command.getPassword()))
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .username(command.getUsername())
                .role(SellerRole.USER)
                .build();
        sellerRepository.save(seller);
        return LoginErrorCode.SUCCESS;
    }

    @Transactional
    public void sendSignUpValidationMail(EmailValidateCommand command) {
        if(sellerRepository.findByEmail(command.getEmailAddress()).isPresent())
            throw new InvalidValueException(ErrorCode.DUPLICATED_EMAIL);
        String secretCode = secretCodeFactory.generate();
        EmailMessage emailMessage = emailMessageFactory.emailValidateMessage(command.getEmailAddress(), secretCode);
        secretCodeService.saveSignUpCode(command.getEmailAddress(), secretCode);
        emailSender.send(emailMessage);
    }

    @Transactional(readOnly = true)
    public void checkSignUpValidationCode(CheckEmailValidateCriteria criteria) {
        String code = secretCodeService.findSighUpCode(criteria.getEmailAddress());
        if(!code.equals(criteria.getCode()))
            throw new InvalidValueException(ErrorCode.NOT_MATCH_CODE);
    }

    public void setTemPassword(String email, String password){
        Seller seller = sellerRepository.findByEmail(email).get();
        seller.changePassword(passwordEncoder.encode(password));
        sellerRepository.save(seller);
    }
    public void changePassword(Seller seller, String password){
        seller.changePassword(passwordEncoder.encode(password));
        sellerRepository.save(seller);
    }

    public Optional<Seller> findSellerByEmail(String email){
        return sellerRepository.findByEmail(email);
    }

    public Optional<Seller> findSellerById(String loginId){
        return sellerRepository.findByLoginId(loginId);
    }

    @Transactional
    public void deleteSeller(Seller seller, SellerPasswordCommand password){
        if(passwordEncoder.matches(password.getPassword(),seller.getPassword())){
            sellerRepository.delete(seller);
        }
    }
}
