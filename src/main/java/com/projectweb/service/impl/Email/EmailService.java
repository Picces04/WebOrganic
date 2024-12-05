package com.projectweb.service.impl.Email;

import com.projectweb.model.PasswordResetToken;
import com.projectweb.reponsitory.token.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TokenRepo tokenRepo;

    // Phương thức gửi email
    public void sendPasswordResetEmail(String userEmail, String resetToken) {

        // Tính toán thời gian hết hạn token, ví dụ là 1 giờ sau khi tạo
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);

        // Lưu token vào cơ sở dữ liệu
        PasswordResetToken token = new PasswordResetToken(userEmail, resetToken, expiresAt);
        tokenRepo.save(token);

        String resetUrl = "http://localhost:8080/password/reset-form?token=" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("khanhhaivodoi@gmail.com"); // Địa chỉ email của bạn
        message.setTo(userEmail);               // Địa chỉ email của người dùng
        message.setSubject("Lấy lại mật khẩu");
        message.setText("Để lấy lại mật khẩu, Vui lòng sử dụng mã sau: " + resetUrl);

        mailSender.send(message);
    }

}

