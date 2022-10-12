package be.shop.slow_delivery.seller.domain;

import be.shop.slow_delivery.seller.application.dto.EmailMessage;

public interface EmailSender {
    void send(EmailMessage emailMessage);
}
