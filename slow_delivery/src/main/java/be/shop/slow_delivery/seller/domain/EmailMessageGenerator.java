package be.shop.slow_delivery.seller.domain;

import be.shop.slow_delivery.seller.application.dto.EmailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageGenerator {

    public EmailMessage emailValidateMessage(String sendTo, String randomCode) {
        StringBuilder body = new StringBuilder();
        body.append("<div style='margin:100px;'>");
        body.append("<h1> 안녕하세요. 느린배달 입니다.</h1>");
        body.append("<br>");
        body.append("<p> 아래 코드를 회원가입 창으로 돌아가 입력해주세요.<p>");
        body.append("<br>");
        body.append("<p>감사합니다.<p>");
        body.append( "<br>");
        body.append( "<div align='center' style='border:1px solid black; font-family:verdana';>");
        body.append( "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>");
        body.append( "<div style='font-size:130%'>");
        body.append( "CODE : <strong>");
        body.append(randomCode);
        body.append("</strong><div><br/> ");
        body.append( "</div>");

        return EmailMessage.builder()
                .emailAddress(sendTo)
                .title("느린배달 회원가입 이메일 인증")
                .body(body.toString())
                .build();
    }
}
