package be.shop.slow_delivery.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static be.shop.slow_delivery.exception.ErrorCode.REQUEST_PARAMETER;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("[handleBusinessException] : {}", e.getErrorCode());
        return ErrorResponse.toResponse(e.getErrorCode());
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e) {
        log.warn("[handleIllegalArgumentException] : {}", e.getMessage());
        return ErrorResponse.toResponse(REQUEST_PARAMETER);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> methodArgumentValidation(MethodArgumentNotValidException e) {
        log.warn("[exception - {}] -> {}", REQUEST_PARAMETER, e.getFieldErrors().stream()
                .map(err-> err.getDefaultMessage()).collect(Collectors.joining(" and ")));
        return ErrorResponse.toResponse(REQUEST_PARAMETER);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> emptyRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("[handleMissingServletRequestParameterException] : {}", e.getMessage());
        return ErrorResponse.toResponse(REQUEST_PARAMETER);
    }
}
