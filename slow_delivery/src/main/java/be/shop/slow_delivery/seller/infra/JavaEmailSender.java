package be.shop.slow_delivery.seller.infra;

import be.shop.slow_delivery.seller.application.dto.EmailMessage;
import be.shop.slow_delivery.seller.domain.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class JavaEmailSender implements EmailSender {
    private final JavaMailSender emailSender;

    @Override
    public void send(EmailMessage emailMessage) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            // 보내는 대상
            message.addRecipients(Message.RecipientType.TO, emailMessage.getEmailAddress());
            // 제목
            message.setSubject(emailMessage.getTitle());
            // 내용
            message.setText(emailMessage.getBody(), "utf-8", "html");
            // 보내는 사람
            message.setFrom(new InternetAddress("slowdelivery22@gmail.com","slowdelivery"));
            emailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
