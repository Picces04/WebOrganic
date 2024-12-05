package com.projectweb.controller.admin;

import com.projectweb.model.OgnUser;
import com.projectweb.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;

@Controller
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager; // Quản lý xác thực

    @Autowired
    private UserService userService;

    @GetMapping("/layout/AdUser")
    public String editdata(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "s", required = false) String search, Model model) {
        // Lấy dữ liệu từ userService, giả sử userService.findAll trả về Page<OgnUser>
        Page<OgnUser> data = userService.findAll(search, pageNumber);

        // Tính toán trang bắt đầu và kết thúc để hiển thị chỉ 3 trang xung quanh trang hiện tại
        int totalPages = data.getTotalPages();
        int visiblePages = 1; // Số trang hiển thị xung quanh trang hiện tại

        // Tính toán startPage và endPage
        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Thêm các giá trị cần thiết vào model
        model.addAttribute("s", search);
        model.addAttribute("data", data.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/layout/AdUser";
    }


    // CRUD USER
    @PostMapping("/insert")
    public String insertUser(OgnUser user, Model model) {
        try {
            user.setCreateddate(Instant.now());
            user.setRole("USER");
            userService.insertUser(user); // Gọi hàm insert từ UserService
            model.addAttribute("message", "Thêm người dùng thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi thêm người dùng!");
        }
        return "redirect:/admin/layout/AdUser";
    }

    // Phương thức cập nhật người dùng
    @PostMapping("/update")
    public String updateUser(@ModelAttribute OgnUser user,@RequestHeader(value = "referer", required = false) String referer, Model model) {
        try {
            OgnUser updatedUser = userService.updateUser(user); // Gọi hàm update từ UserService
            if (updatedUser != null) {
                model.addAttribute("message", "Cập nhật người dùng thành công!");
            } else {
                model.addAttribute("error", "Người dùng không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        // Lấy danh sách người dùng sau khi cập nhật và đưa vào model để hiển thị

        // Chuyển hướng về trang trước đó hoặc mặc định về index nếu không có referer
        return "redirect:" + (referer != null ? referer : "/home/user/index");
    }

    // Phương thức xóa người dùng
    @GetMapping("/delete")
    public String delete(@RequestParam("userid") String userId, RedirectAttributes redirectAttributes) {
        try {
            boolean isDeleted = userService.deleteUser(userId); // Kiểm tra nếu người dùng được xóa thành công
            if (isDeleted) {
                redirectAttributes.addFlashAttribute("message", "Xóa người dùng thành công!"); // Thêm thông báo vào flash attribute
            } else {
                redirectAttributes.addFlashAttribute("error", "Vui lòng xóa admin trước!"); // Thêm thông báo lỗi vào flash attribute
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa người dùng!"); // Thêm thông báo lỗi vào flash attribute
        }

        return "redirect:/admin/layout/AdUser"; // Trả về trang hiển thị người dùng
    }



    // CRUD USER
}
