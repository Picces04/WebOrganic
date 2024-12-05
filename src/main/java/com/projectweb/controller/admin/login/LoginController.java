package com.projectweb.controller.admin.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class LoginController {

//    Login
    @GetMapping("/security/ADauthentication-login")
    public String login() {
        return "admin/security/ADauthentication-login";
    }


    @GetMapping("/security/authentication-register")
    public String register() {
        return "admin/security/authentication-register";
    }
    @GetMapping("/security/authentication-forgot-password")
    public String repass() {
        return "admin/security/authentication-forgot-password";
    }
//    Login

}
