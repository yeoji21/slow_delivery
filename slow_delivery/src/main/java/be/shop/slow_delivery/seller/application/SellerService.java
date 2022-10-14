package be.shop.slow_delivery.seller.application;

import be.shop.slow_delivery.exception.ErrorCode;
import be.shop.slow_delivery.exception.InvalidValueException;
import be.shop.slow_delivery.exception.LoginErrorCode;
import be.shop.slow_delivery.jwt.JwtFilter;
import be.shop.slow_delivery.jwt.TokenProvider;
import be.shop.slow_delivery.seller.application.dto.*;
import be.shop.slow_delivery.seller.domain.*;
import be.shop.slow_delivery.seller.presentation.dto.SellerSignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final EmailSender emailSender;
    private final SecretCodeService secretCodeService;
    private final EmailMessageGenerator emailMessageGenerator;

    @Transactional
    public LoginErrorCode signUp(SellerSignUpCommand command) {
        if(findSellerById(command.getLoginId()).isPresent())
            return LoginErrorCode.DUPLICATE_EMAIL;

        // join method 그대로 복사
        Authority authority = new Authority("ROLE_USER");
        Seller seller = new Seller(command.getLoginId(), passwordEncoder.encode(command.getPassword()),
                command.getEmail(), command.getPhoneNumber(), command.getUsername());
        sellerRepository.save(seller);

        return LoginErrorCode.SUCCESS;
    }

    @Transactional
    public void emailValidate(EmailValidateCommand command) {
        if(sellerRepository.findByEmail(command.getEmailAddress()).isPresent())
            throw new InvalidValueException(ErrorCode.DUPLICATED_EMAIL);

        String secretCode = secretCodeService.generateEmailValidateCode(command.getEmailAddress());
        EmailMessage emailMessage = emailMessageGenerator.emailValidateMessage(command.getEmailAddress(), secretCode);
        emailSender.send(emailMessage);
    }

    @Transactional
    public void join(SellerSignUpDto sellerSignUpDto){
        Authority authority = new Authority("ROLE_USER");

        Seller seller = new Seller(sellerSignUpDto.getLoginId(), passwordEncoder.encode(sellerSignUpDto.getPassword()),
                sellerSignUpDto.getEmail(), sellerSignUpDto.getPhoneNumber(), sellerSignUpDto.getUsername());

        sellerRepository.save(seller);
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

    public SellerLoginCriteria login(SellerLoginCommand sellerLoginCommand) throws Exception{
        Optional<Seller> loginSeller = sellerRepository.findByLoginId(sellerLoginCommand.getLoginId());
        if((loginSeller.orElse(null)==null) || !passwordEncoder.matches(sellerLoginCommand.getPassword(), loginSeller.get().getPassword())) {
            throw new Exception("아이디와 비밀번호가 일치하지 않습니다.");
        } else{
            Seller seller = loginSeller.get();
            TokenCriteria tokenCriteria = getTokenCriteriaEntity(sellerLoginCommand);
            return new SellerLoginCriteria(
                    tokenCriteria.getToken(),
                    seller.getId(),
                    seller.getLoginId(),
                    seller.getEmail()
            );
        }
    }

    private TokenCriteria getTokenCriteriaEntity(SellerLoginCommand sellerLoginCommand){
        Seller seller = sellerRepository.findByLoginId(sellerLoginCommand.getLoginId()).get();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(seller.getEmail(),seller.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(seller.getId(),authentication);

        HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER,"Bearer "+jwt);

        return new TokenCriteria(jwt);
    }
}
