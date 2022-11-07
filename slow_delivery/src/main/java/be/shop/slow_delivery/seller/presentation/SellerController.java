package be.shop.slow_delivery.seller.presentation;

import be.shop.slow_delivery.exception.LoginErrorCode;
import be.shop.slow_delivery.exception.LoginErrorResponse;
import be.shop.slow_delivery.seller.application.EmailServiceImpl;
import be.shop.slow_delivery.seller.application.SellerService;
import be.shop.slow_delivery.seller.application.dto.SellerLoginCommand;
import be.shop.slow_delivery.seller.application.dto.SellerLoginResult;
import be.shop.slow_delivery.seller.domain.Seller;
import be.shop.slow_delivery.seller.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class SellerController {
    private final SellerService sellerService;
    private final EmailServiceImpl emailServiceImpl;
    private final SellerDtoMapper sellerDtoMapper;

    @PostMapping("/seller")
    public LoginErrorResponse<?> signUp(@RequestBody @Valid SellerSignUpDto sellerSignUpDto){
        LoginErrorCode loginErrorCode = sellerService.signUp(sellerDtoMapper.toCommand(sellerSignUpDto));
        return new LoginErrorResponse<>(loginErrorCode);
    }

    @PostMapping("/seller/email-validate")
    public void sendSignUpValidateEmail(@RequestBody @Valid EmailValidateDto emailValidateDto) {
        sellerService.sendSignUpValidationMail(sellerDtoMapper.toCommand(emailValidateDto));
    }

    @GetMapping("/seller/email-validate")
    public void verifySignUpValidateEmail(@RequestBody @Valid CheckEmailValidateDto checkEmailValidateDto) {
        sellerService.checkSignUpValidationCode(sellerDtoMapper.toCriteria(checkEmailValidateDto));
    }

    @PatchMapping("seller/changePw") //비밀번호 변경
    public LoginErrorResponse<?> changePw(Authentication authentication,
                                          @RequestBody PasswordCommand passwordCommand){
        try{
            Seller seller = (Seller) authentication.getPrincipal();
            sellerService.changePassword(seller,passwordCommand.getPassword());
            return new LoginErrorResponse<>(LoginErrorCode.SUCCESS);
        } catch (Exception e) {
            return new LoginErrorResponse<>(LoginErrorCode.INVALID_JWT);
        }
    }

    @PatchMapping("seller/temPassword") //임시 비밀번호 발급
    public LoginErrorResponse<?> setTemPw(@RequestBody EmailCriteria emailCriteria) throws Exception{
        Optional<Seller> loginId = sellerService.findSellerById(emailCriteria.getEmail());

        if(loginId.orElse(null) == null){
            return new LoginErrorResponse<>(LoginErrorCode.NOT_FOUND_ID);
        }
        emailServiceImpl.sendTempPwMessage(emailCriteria.getEmail());
        sellerService.setTemPassword(emailCriteria.getEmail(),emailServiceImpl.tempPw);

        return new LoginErrorResponse<>(LoginErrorCode.SUCCESS);
    }

    @PostMapping("/seller/login") //로그인
    public SellerLoginResult login (@Valid @RequestBody SellerLoginCommand sellerLoginCommand){
        return sellerService.login(sellerLoginCommand);
    }

    @GetMapping("seller/findId") //아이디 찾기
    public LoginErrorResponse<?> findSellerId(@RequestBody EmailCriteria emailCriteria)throws Exception{
        Optional<Seller> seller = sellerService.findSellerByEmail(emailCriteria.getEmail());
        if(seller.isEmpty()){
            return new LoginErrorResponse<>(LoginErrorCode.NOT_FOUND_ID);
        } else{
            emailServiceImpl.sendFindSellerMessage(seller.get().getLoginId(),emailCriteria.getEmail());
            return new LoginErrorResponse<>(LoginErrorCode.SUCCESS);
        }
    }
}
