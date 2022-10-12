package be.shop.slow_delivery.seller.application.dto;

import lombok.Getter;

@Getter
public class EmailValidateCommand {
    String emailAddress;

    public EmailValidateCommand(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
