package com.projectweb.controller.admin;

import com.projectweb.model.OgnCategory;
import com.projectweb.service.admin.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/layout/AdCategory")
    public String editdata(Model model) {
        List<OgnCategory> dataCategory = categoryService.findAll();
        model.addAttribute("dataCategory", dataCategory);
        return "admin/layout/AdCategory";
    }

    // CRUD Category
    private static final String UPLOAD_DIR = "C:/PC/Java IJ/Project/ProjectWeb/src/main/resources/static/assets/images/category/";

    @PostMapping("/insertCategory")
    public String insertCategory(@RequestParam("categoryimages1") MultipartFile categoryimages1, OgnCategory category, Model model) {
        try {
            // Kiểm tra xem file đã được chọn chưa
            if (categoryimages1.isEmpty()) {
                // Nếu không chọn ảnh mới, kiểm tra xem blog có ảnh cũ chưa
                if (category.getCategoryimages() == null || category.getCategoryimages().isEmpty()) {
                    model.addAttribute("error", "Vui lòng chọn ảnh.");
                    return "redirect:/admin/layout/AdCategory"; // Trả về lại trang nếu không chọn ảnh và không có ảnh cũ
                }
                // Nếu không có ảnh mới và đã có ảnh cũ, bỏ qua phần lưu ảnh
            } else {
                // Lấy tên tệp ảnh
                String fileName = categoryimages1.getOriginalFilename();
                // Đường dẫn đầy đủ đến thư mục lưu trữ ảnh
                File destinationDir = new File(UPLOAD_DIR);

                // Đường dẫn đến tệp ảnh
                File destinationFile = new File(destinationDir, fileName);

                // Kiểm tra xem tệp ảnh đã tồn tại trong thư mục chưa
                if (!destinationFile.exists()) {
                    // Nếu ảnh chưa tồn tại, tiến hành lưu ảnh mới
                    categoryimages1.transferTo(destinationFile); // Lưu tệp vào thư mục
                }

                // Lưu đường dẫn ảnh vào OgnBlog
                category.setCategoryimages("/assets/images/category/" + fileName);  // Lưu đường dẫn trong CSDL
            }

            categoryService.insertCategory(category); // Gọi hàm insert từ AdminService
            model.addAttribute("messageCategory", "Thêm danh mục thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorCategory", "Có lỗi xảy ra khi thêm admin!");
        }
        return "redirect:/admin/layout/AdCategory";
    }

    // Phương thức cập nhật người dùng
    @PostMapping("/updateCategory")
    public String updateCategory(@RequestParam("categoryimages2") MultipartFile categoryimages2, @ModelAttribute OgnCategory category, Model model) {
        try {
            // Kiểm tra xem file đã được chọn chưa
            if (categoryimages2.isEmpty()) {
            } else {
                // Lấy tên tệp ảnh
                String fileName = categoryimages2.getOriginalFilename();
                // Đường dẫn đầy đủ đến thư mục lưu trữ ảnh
                File destinationDir = new File(UPLOAD_DIR);

                // Đường dẫn đến tệp ảnh
                File destinationFile = new File(destinationDir, fileName);

                // Kiểm tra xem tệp ảnh đã tồn tại trong thư mục chưa
                if (!destinationFile.exists()) {
                    // Nếu ảnh chưa tồn tại, tiến hành lưu ảnh mới
                    categoryimages2.transferTo(destinationFile); // Lưu tệp vào thư mục
                }

                // Lưu đường dẫn ảnh vào OgnBlog
                category.setCategoryimages("/assets/images/category/" + fileName);  // Lưu đường dẫn trong CSDL
            }

            OgnCategory updatedCategory = categoryService.updateCategory(category); // Gọi hàm update từ AdminService
            if (updatedCategory != null) {
                model.addAttribute("messageCategory", "Cập nhật danh mục thành công!");
            } else {
                model.addAttribute("errorCategory", "Danh mục không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        return "redirect:/admin/layout/AdCategory"; // Trả về trang hiển thị người dùng
    }

    // Phương thức xóa người dùng
    @GetMapping("/deleteCategory")
    public String deleteCategory(@RequestParam("id") long id, Model model) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/layout/AdCategory";
    }
    // CRUD Category
}
