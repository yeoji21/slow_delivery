package be.shop.slow_delivery.exception;

public class InvalidValueException extends BusinessException{
    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
