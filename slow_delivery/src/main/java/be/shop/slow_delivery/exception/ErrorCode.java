package be.shop.slow_delivery.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    // 400 BAD_REQUEST : 잘못된 요청
    REQUEST_PARAMETER(BAD_REQUEST, "잘못된 요청 형식입니다."),
    PHONE_NUMBER(BAD_REQUEST, "잘못된 전화번호 형식입니다."),
    CATEGORY_COUNT(BAD_REQUEST, "가게는 카테고리를 한 개 이상 가져야 합니다."),


    // 404 NOT_FOUND : Resource를 찾을 수 없음
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
