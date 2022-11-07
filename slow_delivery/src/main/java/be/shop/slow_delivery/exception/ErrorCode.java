package be.shop.slow_delivery.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 200 OK
    SUCCESS(OK,  "요청에 성공했습니다."),

    // 400 BAD_REQUEST : 잘못된 요청
    ACCESS_DENIED_LOGIN(BAD_REQUEST, "아이디와 비밀번호가 일치하지 않습니다."),
    NOT_MATCH_CODE(BAD_REQUEST, "코드가 일치하지 않습니다."),
    INVALID_JWT(BAD_REQUEST, "토큰이 없거나, 유효하지 않습니다. 로그인을 해주세요"),
    REQUEST_PARAMETER(BAD_REQUEST, "잘못된 요청 형식입니다."),
    PHONE_NUMBER(BAD_REQUEST, "잘못된 전화번호 형식입니다."),
    DUPLICATED_EMAIL(BAD_REQUEST, "이미 존재하는 사용자의 이메일입니다."),
    DUPLICATE_ID(BAD_REQUEST, "중복된 아이디 입니다."),
    CATEGORY_COUNT(BAD_REQUEST, "가게는 카테고리를 한 개 이상 가져야 합니다."),

    // 404 NOT_FOUND : Resource를 찾을 수 없음
    SELLER_NOT_FOUND(NOT_FOUND,"판매자를 찾을 수 없습니다."),
    ID_NOT_FOUND(NOT_FOUND,"해당 이메일로 가입된 아이디가 존재하지 않습니다."),
    SHOP_NOT_FOUND(NOT_FOUND, "해당 가게를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(NOT_FOUND, "해당 카테고리를 찾을 수 없습니다"),
    PRODUCT_NOT_FOUND(NOT_FOUND, "해당 상품을 찾을 수 없습니다"),
    STOCK_NOT_FOUND(NOT_FOUND, "해당 재고를 찾을 수 없습니다."),
    MENU_NOT_FOUND(NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),

    // 503 SERVICE_UNAVAILABLE : 서비스 이용 불가
    REDIS_UNAVAILABLE(SERVICE_UNAVAILABLE, "레디스 서버에 문제가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
