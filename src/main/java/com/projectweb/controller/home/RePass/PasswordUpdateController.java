package com.projectweb.controller.home.RePass;

import com.projectweb.model.OgnUser;
import com.projectweb.model.PasswordResetToken;
import com.projectweb.reponsitory.token.TokenRepo;
import com.projectweb.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

@Controller
public class PasswordUpdateController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenRepo tokenRepository;

    @PostMapping("/password/update")
    public String updatePassword(@RequestParam("token") String token,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp.");
            return "/admin/security/authentication-forgot-password"; // Trả lại trang nếu mật khẩu không khớp
        }

        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token).orElse(null);
        if (passwordResetToken != null && passwordResetToken.getExpiresAt().isAfter(LocalDateTime.now())) {
            // Token hợp lệ
            OgnUser user = userService.findByEmail(passwordResetToken.getEmail());
            user.setPasswordhash(newPassword);
            userService.updateUser(user); // Cập nhật mật khẩu
            return "redirect:/admin/security/ADauthentication-login"; // Trang thành công
        } else {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            return "error"; // Trang lỗi
        }
    }
}

