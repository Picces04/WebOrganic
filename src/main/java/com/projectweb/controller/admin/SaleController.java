package com.projectweb.controller.admin;

import com.projectweb.model.OgnSaleprice;
import com.projectweb.service.admin.CategoryService;
import com.projectweb.service.admin.ProductService;
import com.projectweb.service.admin.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/layout/AdSale")
    public String editdata(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                                @RequestParam(value = "s", required = false) String search, Model model) {
        Page<OgnSaleprice> dataSale = saleService.findAll(search, pageNumber);

        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("productList", productService.findAll());
        // Tính toán trang bắt đầu và kết thúc để hiển thị chỉ 1 trang xung quanh trang hiện tại
        int totalPages = dataSale.getTotalPages();
        int visiblePages = 1; // Số trang hiển thị xung quanh trang hiện tại

        // Tính toán startPage và endPage
        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Thêm các giá trị cần thiết vào model
        model.addAttribute("s", search);
        model.addAttribute("dataSale", dataSale.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "admin/layout/AdSale";
    }

    // CRUD Product
    @PostMapping("/insertSale")
    public String insertSale(OgnSaleprice saleprice, Model model) {
        try {
            saleService.insertSale(saleprice); // Gọi hàm insert từ AdminService
            model.addAttribute("messageSale", "Thêm sản phẩm thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorSale", "Có lỗi xảy ra khi thêm admin!");
        }
        return "redirect:/admin/layout/AdSale";
    }

    // Phương thức cập nhật người dùng
    @PostMapping("/updateSale")
    public String updateSale(@ModelAttribute OgnSaleprice saleprice, Model model) {
        try {
            OgnSaleprice updatedSale = saleService.updateSale(saleprice); // Gọi hàm update từ AdminService
            if (updatedSale != null) {
                model.addAttribute("messageSale", "Cập nhật sản phẩm thành công!");
            } else {
                model.addAttribute("errorSale", "Sản phẩm không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        return "redirect:/admin/layout/AdSale"; // Trả về trang hiển thị người dùng
    }

    // Phương thức xóa người dùng
    @GetMapping("/deleteSale")
    public String deleteSale(@RequestParam("id") long id, Model model) {
        saleService.deleteSale(id);
        return "redirect:/admin/layout/AdSale";
    }
    // CRUD Product

}
