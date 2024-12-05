package com.projectweb.config;

import com.projectweb.config.security.CustomAuthenticationSuccessHandler;
import com.projectweb.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    @Lazy
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/security/facebook-auth").permitAll()
                        .requestMatchers("/**", "/home/**", "/admin/security/**").permitAll()
                        .requestMatchers("/admin/layout/**").hasAuthority("ADMIN") // Chỉ ADMIN truy cập
                        .anyRequest().authenticated() // Các yêu cầu khác phải xác thực
                )
                .formLogin(login -> login
                        .loginPage("/admin/security/ADauthentication-login")
                        .successHandler(customAuthenticationSuccessHandler()) // Sử dụng handler tuỳ chỉnh
                        .failureUrl("/admin/security/ADauthentication-login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/security/ADauthentication-login")
                        .invalidateHttpSession(true) // Xóa tất cả session
                        .deleteCookies("JSESSIONID") // Xóa cookie liên quan đến session
                        .permitAll()
                );
        http
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/admin/security/ADauthentication-login") // Trang đăng nhập tùy chỉnh
                        .defaultSuccessUrl("/loginSuccess", true) // Xử lý thành công
                        .failureUrl("/admin/security/ADauthentication-login?error=true") // Trang chuyển hướng khi đăng nhập thất bại
                );

        return http.build();
    }


    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}
