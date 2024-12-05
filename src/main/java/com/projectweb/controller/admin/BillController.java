package com.projectweb.controller.admin;

import com.projectweb.model.OgnBillingDetail;
import com.projectweb.model.OgnOrder;
import com.projectweb.model.OgnOrderdetail;
import com.projectweb.reponsitory.admin.BillReponsitory;
import com.projectweb.service.admin.BillService;
import com.projectweb.service.admin.OrderDetailsService;
import com.projectweb.service.admin.OrderService;
import com.projectweb.service.impl.admin.BillSerivceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class BillController {
    @Autowired
    private AuthenticationManager authenticationManager; // Quản lý xác thực

    @Autowired
    private BillService billService;

    @GetMapping("/layout/AdBill")
    public String editAdmindata(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                                @RequestParam(value = "s", required = false) String search, HttpSession session, Model model) {
        Page<OgnBillingDetail> dataBill = billService.findAll(pageNumber,search);


        // Tính toán trang bắt đầu và kết thúc để hiển thị chỉ 1 trang xung quanh trang hiện tại
        int totalPages = dataBill.getTotalPages();
        int visiblePages = 1; // Số trang hiển thị xung quanh trang hiện tại

        // Tính toán startPage và endPage
        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Thêm các giá trị cần thiết vào model
        model.addAttribute("s", search);
        model.addAttribute("dataBill", dataBill.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "admin/layout/AdBill";
    }

    // CRUD ADMIN
    @PostMapping("/insertBill")
    public String insertAdmin(OgnBillingDetail billingDetail, Model model) {
        try {
            billService.insertBill(billingDetail); // Gọi hàm insert từ AdminService
            model.addAttribute("message", "Thêm admin thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi thêm admin!");
        }
        return "redirect:/admin/layout/AdBill";
    }

    // Phương thức cập nhật người dùng
    @PostMapping("/updateBill")
    public String updateAdmin(@ModelAttribute OgnBillingDetail billingDetail, Model model) {
        try {
            OgnBillingDetail updatedBill = billService.updateBill(billingDetail); // Gọi hàm update từ AdminService
            if (updatedBill != null) {
                model.addAttribute("message", "Cập nhật admin thành công!");
            } else {
                model.addAttribute("error", "Admin không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        return "redirect:/admin/layout/AdBill"; // Trả về trang hiển thị người dùng
    }

    // Phương thức xóa người dùng
    @GetMapping("/deleteBill")
    public String deleteAdmin(@RequestParam("id") long id, Model model) {
        billService.deleteBill(id);
        return "redirect:/admin/layout/AdBill";
    }
    // CRUD ADMIN


}
