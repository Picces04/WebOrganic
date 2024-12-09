package com.projectweb.controller;

import com.projectweb.service.admin.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AllController {

    @Autowired
    private AuthenticationManager authenticationManager; // Quản lý xác thực

    @Autowired
    private ProductService productService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/layout/index")
    @PreAuthorize("@adminSerivceImpl.validatePermission('ADMIN')")
    public String index(Model model) {
        Integer totalProduct = productService.Totalproduct();
        model.addAttribute("totalProduct", totalProduct);

        Integer totalBlog = blogService.TotalBlogs();
        model.addAttribute("totalBlog", totalBlog);

        Integer totalReview = reviewService.Totalreview();
        model.addAttribute("totalReview", totalReview);

        Integer totalUser = userService.TotalUsers();
        model.addAttribute("totalUser", totalUser);



        return "admin/layout/index";
    }


//    Login


//    Login

}
