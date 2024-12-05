package com.projectweb.controller.home;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home/security")
public class HomeLoginController {
    //Login
    @GetMapping("/authentication-login")
    public String login() {
        return "home/security/authentication-login";
    }
    @GetMapping("/authentication-register")
    public String register() {
        return "home/security/authentication-register";
    }
    //Login
}
