package com.projectweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AllController {

    @Autowired
    private AuthenticationManager authenticationManager; // Quản lý xác thực

    @GetMapping("/layout/index")
    @PreAuthorize("@adminSerivceImpl.validatePermission('ADMIN')")
    public String index() {
        return "admin/layout/index";
    }


//    Login


//    Login

}
