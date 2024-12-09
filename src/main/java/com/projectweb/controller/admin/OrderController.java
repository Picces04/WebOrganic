package com.projectweb.controller.admin;

import com.projectweb.model.OgnAdmin;
import com.projectweb.model.OgnOrder;
import com.projectweb.service.admin.AdminService;
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
public class OrderController {
    @Autowired
    private AuthenticationManager authenticationManager; // Quản lý xác thực

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/layout/AdOrder")
    public String editAdmindata(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "s", required = false) String search, Model model) {
        Page<OgnOrder> dataOrder = orderService.findAll(pageNumber,search);
        model.addAttribute("userList", userService.findAll());
        // Tính toán trang bắt đầu và kết thúc để hiển thị chỉ 1 trang xung quanh trang hiện tại
        int totalPages = dataOrder.getTotalPages();
        int visiblePages = 1; // Số trang hiển thị xung quanh trang hiện tại

        // Tính toán startPage và endPage
        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Thêm các giá trị cần thiết vào model
        model.addAttribute("s", search);
        model.addAttribute("dataOrder", dataOrder.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "admin/layout/AdOrder";
    }

    // CRUD ADMIN
    @PostMapping("/insertOrder")
    public String insertAdmin(OgnOrder order, Model model) {
        try {
            orderService.insertOrder(order); // Gọi hàm insert từ AdminService
            model.addAttribute("message", "Thêm admin thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi thêm admin!");
        }
        return "redirect:/admin/layout/AdOrder";
    }

    // Phương thức cập nhật người dùng
    @PostMapping("/updateOrder")
    public String updateAdmin(@ModelAttribute OgnOrder order, @RequestHeader(value = "referer", required = false) String referer, Model model) {
        try {
            OgnOrder updatedOrder = orderService.updateOrder(order); // Gọi hàm update từ AdminService
            if (updatedOrder != null) {
                model.addAttribute("message", "Cập nhật admin thành công!");
            } else {
                model.addAttribute("error", "Admin không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        return "redirect:" + (referer != null ? referer : "/home/user/index");
    }

    // Phương thức xóa người dùng
    @GetMapping("/deleteOrder")
    public String deleteAdmin(@RequestParam("id") long id, Model model) {
        orderService.deleteOrder(id);
        return "redirect:/admin/layout/AdOrder";
    }
    // CRUD ADMIN


}
