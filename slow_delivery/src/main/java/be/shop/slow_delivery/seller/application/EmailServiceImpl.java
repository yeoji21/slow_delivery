package be.shop.slow_delivery.seller.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    JavaMailSender emailSender;

    public static String ePw;
    public static String tempPw;

    private MimeMessage createMessage(String to) throws Exception{
        ePw = createKey();
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to); //보내는 대상
        message.setSubject("느린배달 회원가입 이메일 인증"); //제목

        String msgg="";
        msgg+="<div style='margin:100px;'>";
        msgg+="<h1> 안녕하세요. 느린배달 입니다.</h1>";
        msgg+="<br>";
        msgg+="<p> 아래 코드를 회원가입 창으로 돌아가 입력해주세요.<p>";
        msgg+="<br>";
        msgg+="<p>감사합니다.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+=ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("slowdelivery22@gmail.com","slowdelivery"));//보내는 사람

        return message;

    }

    private MimeMessage createTempPwMessage(String to) throws Exception{
        tempPw = createKey();
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO,to);
        message.setSubject("느린배달  임시 비밀번호");

        String msgg="";
        msgg+="<div style='margin:100px;'>";
        msgg+="<h1> 안녕하세요. 느린배달 입니다.</h1>";
        msgg+="<br>";
        msgg+="<p> 임시 비밀번호가 발급되었습니다.<p>";
        msgg+="<br>";
        msgg+="<p>감사합니다.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>아래의 비밀번호로 로그인 해주세요.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+=ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("slowdelivery22@gmail.com","slowdelivery"));//보내는 사람

        return message;
    }

    private MimeMessage createFindSellerMessage(String sellerId, String to) throws Exception{
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO,to);
        message.setSubject("느린배달 아이디 찾기");

        String msgg="";
        msgg+="<div style='margin:100px;'>";
        msgg+="<h1> 안녕하세요. 느린배달 입니다.</h1>";
        msgg+="<br>";
        msgg+="<p> 이메일을 받으신 계정으로 가입되어 있는 아이디입니다.<p>";
        msgg+="<br>";
        msgg+="<p>감사합니다.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>아래의 아이디를 확인해주세요.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+=ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("slowdelivery22@gmail.com","slowdelivery"));//보내는 사람

        return message;
    }

    public static String createKey(){

        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        rnd.setSeed(System.currentTimeMillis());

        for(int i=0;i<8;i++){ //인증코드 8자리
            int index = rnd.nextInt(3); //0-2 까지 랜덤

            switch (index){
                case 0:
                    key.append((char) ((int)(rnd.nextInt(26))+97));
                    break;
                case 1:
                    key.append((char) ((int)(rnd.nextInt(26))+65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception{
        // TODO AUTO-generated method stub
        MimeMessage message = createMessage(to);
        try {
            emailSender.send(message);
        } catch (MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }

    @Override
    public String sendTempPwMessage(String to) throws Exception{
        // TODO Auto-generated method stub
        MimeMessage message = createTempPwMessage(to);
        try{
            emailSender.send(message);
        } catch (MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return this.tempPw;
    }
    @Override
    public String sendFindSellerMessage(String sellerId, String to) throws Exception{
        MimeMessage message = createFindSellerMessage(sellerId,to);

        try{
            emailSender.send(message);
        } catch (MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return sellerId;
    }
}
