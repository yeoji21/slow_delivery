package be.shop.slow_delivery.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class LoginErrorResponse<T> {

    private Boolean isSuccess;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    //요청 성공
    public LoginErrorResponse(T result){
        this.isSuccess = LoginErrorCode.SUCCESS.getIsSuccess();
        this.message = LoginErrorCode.SUCCESS.getMessage();
        this.result = result;
    }

    //오류 발생
    public LoginErrorResponse(LoginErrorCode loginErrorCode){
        this.isSuccess = loginErrorCode.getIsSuccess();
        this.message = loginErrorCode.getMessage();
    }
}
