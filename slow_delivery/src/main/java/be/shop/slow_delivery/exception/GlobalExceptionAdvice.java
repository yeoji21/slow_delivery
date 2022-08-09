package be.shop.slow_delivery.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("[handleBusinessException] : {}", e.getErrorCode());
        return ErrorResponse.toResponse(e.getErrorCode());
    }

}
