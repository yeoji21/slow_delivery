package be.shop.slow_delivery.seller.application;

public interface EmailService {
    String sendSimpleMessage(String to)throws Exception;
    String sendTempPwMessage(String to)throws Exception;
    String sendFindSellerMessage(String sellerId, String to) throws Exception;
}
