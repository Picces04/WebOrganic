package com.projectweb.controller.admin;

import com.projectweb.model.OgnAdmin;
import com.projectweb.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AuthenticationManager authenticationManager; // Quản lý xác thực

    @Autowired
    private AdminService adminService;

    @GetMapping("/layout/AdAdmin")
    public String editAdmindata(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "s", required = false) String search, Model model) {
        Page<OgnAdmin> dataAdmin = adminService.findAll(search, pageNumber);
        // Tính toán trang bắt đầu và kết thúc để hiển thị chỉ 1 trang xung quanh trang hiện tại
        int totalPages = dataAdmin.getTotalPages();
        int visiblePages = 1; // Số trang hiển thị xung quanh trang hiện tại

        // Tính toán startPage và endPage
        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Thêm các giá trị cần thiết vào model
        model.addAttribute("s", search);
        model.addAttribute("dataAdmin", dataAdmin.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "admin/layout/AdAdmin";
    }

    // CRUD ADMIN
    @PostMapping("/insertAdmin")
    public String insertAdmin(OgnAdmin admin, Model model) {
        try {
            adminService.insertAdmin(admin); // Gọi hàm insert từ AdminService
            model.addAttribute("messageAdmin", "Thêm admin thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorAdmin", "Có lỗi xảy ra khi thêm admin!");
        }
        return "redirect:/admin/layout/AdAdmin";
    }

    // Phương thức cập nhật người dùng
    @PostMapping("/updateAdmin")
    public String updateAdmin(@ModelAttribute OgnAdmin admin, Model model) {
        try {
            OgnAdmin updatedAdmin = adminService.updateAdmin(admin); // Gọi hàm update từ AdminService
            if (updatedAdmin != null) {
                model.addAttribute("messageAdmin", "Cập nhật admin thành công!");
            } else {
                model.addAttribute("errorAdmin", "Admin không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        return "redirect:/admin/layout/AdAdmin"; // Trả về trang hiển thị người dùng
    }

    // Phương thức xóa người dùng
    @GetMapping("/deleteAdmin")
    public String deleteAdmin(@RequestParam("id") long id, Model model) {
        adminService.deleteAdmin(id);
        return "redirect:/admin/layout/AdAdmin";
    }
    // CRUD ADMIN


}
