package com.projectweb.controller.home.RePass;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Cấu hình SMTP Gmail
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);  // Sử dụng cổng 587 cho STARTTLS
        mailSender.setUsername("khanhhaivodoi@gmail.com");  // Thay thế bằng email của bạn
        mailSender.setPassword("cuzf tstd xqfh gfqk");     // Mật khẩu ứng dụng của bạn

        // Cấu hình STARTTLS
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", "true");  // Bật STARTTLS
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        return mailSender;
    }
}

