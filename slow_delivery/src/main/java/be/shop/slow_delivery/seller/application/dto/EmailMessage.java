package be.shop.slow_delivery.seller.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailMessage {
    private String emailAddress;
    private String title;
    private String body;

    @Builder
    public EmailMessage(String emailAddress,
                        String title,
                        String body) {
        this.emailAddress = emailAddress;
        this.title = title;
        this.body = body;
    }
}
