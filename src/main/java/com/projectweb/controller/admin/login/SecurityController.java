package com.projectweb.controller.admin.login;

import com.projectweb.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    UserService userService;

    @PostMapping("/facebook-auth")
    public ResponseEntity<?> authenticateWithFacebook(@RequestHeader("Authorization") String token) {
        // Xác thực token nhận được từ Facebook hoặc backend
        if (validateFacebookToken(token)) {
            // Tạo đối tượng Authentication
            UserDetails userDetails = userService.loadUserByUsername(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // Cập nhật SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Principal: " + auth.getPrincipal());
            System.out.println("Authorities: " + auth.getAuthorities());

            return ResponseEntity.ok("Authenticated");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    private boolean validateFacebookToken(String token) {
        // Kiểm tra token từ Facebook hoặc backend của bạn
        return true; // Thay bằng logic kiểm tra thật sự
    }

}

