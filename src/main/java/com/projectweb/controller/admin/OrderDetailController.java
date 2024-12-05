package com.projectweb.controller.admin;

import com.projectweb.model.OgnOrder;
import com.projectweb.model.OgnOrderdetail;
import com.projectweb.service.admin.OrderDetailsService;
import com.projectweb.service.admin.OrderService;
import com.projectweb.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class OrderDetailController {
    @Autowired
    private AuthenticationManager authenticationManager; // Quản lý xác thực

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @GetMapping("/layout/AdOrderDetails")
    public String editAdmindata(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "s", required = false) String search, Model model) {
        Page<OgnOrderdetail> dataOrderdetail = orderDetailsService.findAll(pageNumber,search);

        // Tính toán trang bắt đầu và kết thúc để hiển thị chỉ 1 trang xung quanh trang hiện tại
        int totalPages = dataOrderdetail.getTotalPages();
        int visiblePages = 1; // Số trang hiển thị xung quanh trang hiện tại

        // Tính toán startPage và endPage
        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Thêm các giá trị cần thiết vào model
        model.addAttribute("s", search);
        model.addAttribute("dataOrderDetail", dataOrderdetail.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "admin/layout/AdOrderDetails";
    }

    // CRUD ADMIN
    @PostMapping("/insertOrderDetail")
    public String insertAdmin(OgnOrderdetail orderdetail, Model model) {
        try {
            orderDetailsService.insertOrderdetail(orderdetail); // Gọi hàm insert từ AdminService
            model.addAttribute("message", "Thêm admin thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi thêm admin!");
        }
        return "redirect:/admin/layout/AdOrderDetails";
    }

    // Phương thức cập nhật người dùng
    @PostMapping("/updateOrderDetail")
    public String updateAdmin(@ModelAttribute OgnOrderdetail orderdetail, Model model) {
        try {
            OgnOrderdetail updatedOrderdetail = orderDetailsService.updateOrderdetail(orderdetail); // Gọi hàm update từ AdminService
            if (updatedOrderdetail != null) {
                model.addAttribute("message", "Cập nhật admin thành công!");
            } else {
                model.addAttribute("error", "Admin không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        return "redirect:/admin/layout/AdOrderDetails"; // Trả về trang hiển thị người dùng
    }

    // Phương thức xóa người dùng
    @GetMapping("/deleteOrderDetail")
    public String deleteAdmin(@RequestParam("id") long id, Model model) {
        orderDetailsService.deleteOrderdetail(id);
        return "redirect:/admin/layout/AdOrderDetails";
    }
    // CRUD ADMIN


}
