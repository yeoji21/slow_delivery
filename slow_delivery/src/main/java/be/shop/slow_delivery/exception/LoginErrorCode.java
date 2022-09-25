package be.shop.slow_delivery.exception;

import lombok.Getter;

@Getter
public enum LoginErrorCode {

    SUCCESS(true,  "요청에 성공했습니다."),

    INVALID_INPUT_VALUE(false, "입력값을 확인해주세요."),

    INVALID_JWT(false, "토큰이 없거나, 유효하지 않습니다. 로그인을 해주세요"),

    INVALID_REQUEST(false, "잘못된 요청입니다."),

    ACCESS_DENIED_LOGIN(false, "아이디와 비밀번호가 일치하지 않습니다."),

    DUPLICATE_EMAIL(false, "이메일로 가입된 아이디가 존재합니다."),

    NOT_MATCH_CODE(false, "코드가 일치하지 않습니다."),

    NOT_FOUND_ID(false,"이메일로 가입된 아이디가 존재하지 않습니다."),

    DUPLICATE_ID(false, "중복된 아이디 입니다.");


    private Boolean isSuccess;
    private String message;

    LoginErrorCode(Boolean isSuccess, String message){
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
