package com.projectweb.controller.home.RePass;

import com.projectweb.service.impl.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/password")
public class SendEmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/reset")
    public String resetPassword(@RequestParam String email) {
        // Tạo mã reset (có thể là UUID hoặc mã ngẫu nhiên)
        String resetToken = UUID.randomUUID().toString();

        // Gửi email với mã reset đến người dùng
        emailService.sendPasswordResetEmail(email, resetToken);

        String send = "True, Check gmail !";
        return send;
    }
}
