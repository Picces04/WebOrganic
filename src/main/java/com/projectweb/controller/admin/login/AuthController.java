package com.projectweb.controller.admin.login;

import com.projectweb.model.OgnUser;
import com.projectweb.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;

@Controller
public class AuthController {

    @Autowired
    private UserService userService; // Gọi UserService để thêm user vào database

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User principal) {
        // Lấy thông tin từ GitHub
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");

        // Kiểm tra nếu người dùng đã tồn tại
        if (userService.existsByEmail(email)) {
            // Nếu chưa tồn tại, tạo user mới
            OgnUser user = new OgnUser();
            user.setEmail(email);
            user.setPasswordhash("123456");
            user.setUsername(name);
            user.setRole("USER"); // Gán quyền mặc định là USER
            user.setCreateddate(Instant.now());

            // Gọi UserService để thêm user vào database
            userService.insertUser(user);
        }

        // Chuyển hướng đến trang sau đăng nhập thành công
        return "redirect:/home/user/index";
    }
}

