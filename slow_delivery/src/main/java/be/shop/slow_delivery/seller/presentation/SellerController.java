package be.shop.slow_delivery.seller.presentation;

import be.shop.slow_delivery.exception.LoginErrorCode;
import be.shop.slow_delivery.exception.LoginErrorResponse;
import be.shop.slow_delivery.seller.application.EmailServiceImpl;
import be.shop.slow_delivery.seller.application.SellerService;
import be.shop.slow_delivery.seller.application.dto.SellerCommand;
import be.shop.slow_delivery.seller.application.dto.SellerLoginCommand;
import be.shop.slow_delivery.seller.application.dto.SellerLoginCriteria;
import be.shop.slow_delivery.seller.domain.Seller;
import be.shop.slow_delivery.seller.presentation.dto.EmailCriteria;
import be.shop.slow_delivery.seller.presentation.dto.VerifyCodeCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class SellerController {

    private final SellerService sellerService;
    private final EmailServiceImpl emailServiceImpl;

    @PostMapping("/seller/signup")
    public LoginErrorResponse<?> sellerSignUp(SellerCommand sellerCommand) throws Exception{
        Optional<Seller> findSeller = sellerService.findSellerById(sellerCommand.getLoginId());
        try{
            if(findSeller.isPresent()){
                return new LoginErrorResponse<> (LoginErrorCode.DUPLICATE_EMAIL);
            }
            sellerService.join(sellerCommand);
            return new LoginErrorResponse<> (LoginErrorCode.SUCCESS);
        } catch (ConstraintViolationException e){
            return new LoginErrorResponse<> (LoginErrorCode.INVALID_INPUT_VALUE);
        }
    }

    @PostMapping("/mailConfirm") //본인 인증 메일 전송
    public LoginErrorResponse<?> emailConfirm(@RequestBody EmailCriteria emailCriteria) throws Exception{
        Optional<Seller> seller = sellerService.findSellerById(emailCriteria.getEmail());

        if(seller.isEmpty()){
            emailServiceImpl.sendSimpleMessage(emailCriteria.getEmail());
            return new LoginErrorResponse<>(LoginErrorCode.SUCCESS);
        } else{
            return new LoginErrorResponse<>(LoginErrorCode.DUPLICATE_EMAIL);
        }
    }

    @PostMapping("/verifyCode") //본인 인증 코드 일치 여부 확인
    public LoginErrorResponse<?> verifyCode(@RequestBody VerifyCodeCriteria verifyCodeCriteria){
        if(emailServiceImpl.ePw.equals(verifyCodeCriteria.getCode())){
            return new LoginErrorResponse<>(LoginErrorCode.SUCCESS);
        } else
            return new LoginErrorResponse<>(LoginErrorCode.NOT_MATCH_CODE);
    }

    @PostMapping("seller/login") //로그인
    public LoginErrorResponse<?> login (@RequestBody SellerLoginCommand sellerLoginCommand) throws NoSuchElementException{
        try{
            SellerLoginCriteria seller = sellerService.login(sellerLoginCommand);
            return new LoginErrorResponse<>(seller);
        } catch (Exception e){
            return new LoginErrorResponse<>(LoginErrorCode.ACCESS_DENIED_LOGIN);
        }
    }
}
