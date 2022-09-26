package be.shop.slow_delivery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
@PropertySource("classpath:application-email.yml")
public class EmailConfig {
    @Value("587")
    private int port;
    @Value("true")
    private boolean auth;
    @Value("true")
    private boolean starttls;
    @Value("true")
    private boolean starttls_required;

    @Value("slowdelivery22@gmail.com")
    private String email;
    @Value("smgksflsdlqodmaekf")
    private String password;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername(email);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }
    private Properties getMailProperties()
    {
        Properties pt = new Properties();
        pt.put("mail.smtp.auth", auth);
        pt.put("mail.smtp.starttls.enable", starttls);
        pt.put("mail.smtp.starttls.required", starttls_required);
        pt.put("mail.smtp.ssl.protocols", "TLSv1.2");
        return pt;
    }
}
