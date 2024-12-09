package com.projectweb.controller.home.RePass;


import com.projectweb.model.PasswordResetToken;
import com.projectweb.reponsitory.token.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class PasswordResetController {

    @Autowired
    private TokenRepo tokenRepository;

    @GetMapping("/password/reset-form")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Kiểm tra token trong cơ sở dữ liệu
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElse(null);

        if (passwordResetToken != null && passwordResetToken.getExpiresAt().isAfter(LocalDateTime.now())) {
            // Token hợp lệ và chưa hết hạn
            model.addAttribute("token", token);
            return "admin/security/authentication-re-password"; // Hiển thị form nhập mật khẩu mới
        } else {
            // Token không hợp lệ hoặc đã hết hạn
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            return "admin/security/authentication-forgot-password"; // Hiển thị trang lỗi
        }
    }
}
