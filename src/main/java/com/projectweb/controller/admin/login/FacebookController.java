package com.projectweb.controller.admin.login;

import com.projectweb.model.OgnUser;
import com.projectweb.service.admin.UserService;
import com.projectweb.service.impl.Token.JwtService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/facebook")
public class FacebookController {
    @Autowired
    private UserService userService; // Gọi UserService để thêm user vào database

    @Autowired
    private JwtService jwtService; // Gọi UserService để thêm user vào database

    @PostMapping("/login")
    public ResponseEntity<?> loginWithFacebook(@RequestBody Map<String, Object> userData, HttpSession session, Model model) {
        // Lấy thông tin từ client
        String facebookId = userData.get("id").toString();
        String name = userData.get("name").toString();
        String email = userData.get("email").toString();

        // Kiểm tra nếu người dùng đã tồn tại
        OgnUser check = userService.findByEmail(email) ;
        if (userService.existsByEmail(email)){
            // Nếu chưa tồn tại, tạo user mới
            check.setEmail(email);
            check.setPasswordhash("123456");
            check.setUsername(name);
            check.setRole("USER"); // Gán quyền mặc định là USER
            check.setCreateddate(Instant.now());

            // Gọi UserService để thêm user vào database
            userService.insertUser(check);
        }
        //Lấy session git
        if (check != null) {
            OgnUser UserGitSS = (OgnUser) session.getAttribute("UserGitSS");
            if (UserGitSS == null) {
                UserGitSS = new OgnUser() {
                }; // Khởi tạo đối tượng mới
            }
            if (check != null) {
                UserGitSS.setUserid(check.getUserid()); // Ví dụ với trường 'name'
                UserGitSS.setEmail(check.getEmail());
                UserGitSS.setUsername(check.getUsername());
                UserGitSS.setPasswordhash(check.getPasswordhash());
                UserGitSS.setRole(check.getRole());
                UserGitSS.setCreateddate(check.getCreateddate());
                UserGitSS.setPhone(check.getPhone());
                // Thêm các trường khác tùy thuộc vào cấu trúc của OgnUser
            }

            session.setAttribute("UserGitSS", UserGitSS);
            model.addAttribute("UserGitSS", UserGitSS);
        }

        // Trả token hoặc session để đăng nhập thành công
        String token = check.getEmail();
        return ResponseEntity.ok(Map.of("token", token));
    }
}

