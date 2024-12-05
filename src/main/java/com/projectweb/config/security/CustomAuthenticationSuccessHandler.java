package com.projectweb.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Lấy danh sách quyền (roles) của người dùng
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Kiểm tra và chuyển hướng theo vai trò
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ADMIN")) {
                response.sendRedirect("/admin/layout/index");
                return;
            } else if (authority.getAuthority().equals("USER")) {
                String email = authentication.getName();
                response.sendRedirect("/home/user/my-account/" + email);
                return;
            }
        }

        // Mặc định chuyển hướng nếu không có role phù hợp
        response.sendRedirect("/");
    }
}
