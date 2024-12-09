package com.projectweb.controller.admin;

import com.projectweb.model.OgnStarrating;
import com.projectweb.service.admin.ProductService;
import com.projectweb.service.admin.ReviewService;
import com.projectweb.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;

@Controller
@RequestMapping("/admin")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/layout/AdReview")
    public String editdata(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                                @RequestParam(value = "s", required = false) String search, Model model) {
        Page<OgnStarrating> dataReview = reviewService.findAll(search, pageNumber);

        model.addAttribute("productList", productService.findAll());
        model.addAttribute("userList", userService.findAll());
        // Tính toán trang bắt đầu và kết thúc để hiển thị chỉ 1 trang xung quanh trang hiện tại
        int totalPages = dataReview.getTotalPages();
        int visiblePages = 1; // Số trang hiển thị xung quanh trang hiện tại

        // Tính toán startPage và endPage
        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Thêm các giá trị cần thiết vào model
        model.addAttribute("s", search);
        model.addAttribute("dataReview", dataReview.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "admin/layout/AdReview";
    }

    // CRUD Product
    @PostMapping("/insertReview")
    public String insertReview(OgnStarrating review, Model model) {
        try {
            review.setRatingdate(Instant.now());
            reviewService.insertReview(review); // Gọi hàm insert từ AdminService
            model.addAttribute("messageReview", "Thêm sản phẩm thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorReview", "Có lỗi xảy ra khi thêm admin!");
        }
        return "redirect:/admin/layout/AdReview";
    }

    // Phương thức cập nhật người dùng
    @PostMapping("/updateReview")
    public String updateProduct(@ModelAttribute OgnStarrating review, Model model, RedirectAttributes redirectAttributes) {
        try {
            OgnStarrating updatedReview = reviewService.updateReview(review); // Gọi hàm update từ AdminService
            if (updatedReview != null) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật sản phẩm thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorReview", "Sản phẩm không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        model.addAttribute("message", "Cập nhật sản phẩm thành công!");
        return "redirect:/admin/layout/AdReview"; // Trả về trang hiển thị người dùng
    }

    // Phương thức xóa người dùng
    @GetMapping("/deleteReview")
    public String deleteReview(@RequestParam("id") long id, Model model) {
        reviewService.deleteReview(id);
        return "redirect:/admin/layout/AdReview";
    }
    // CRUD Product

}
