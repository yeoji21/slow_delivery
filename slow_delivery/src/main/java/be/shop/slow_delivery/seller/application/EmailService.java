package be.shop.slow_delivery.seller.application;

public interface EmailService {
    String sendTempPwMessage(String to)throws Exception;
    String sendFindSellerMessage(String sellerId, String to) throws Exception;
}
