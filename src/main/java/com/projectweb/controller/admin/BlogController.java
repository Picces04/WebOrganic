package com.projectweb.controller.admin;

import com.projectweb.model.OgnBlog;
import com.projectweb.service.admin.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

@Controller
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    private BlogService BlogService;

    @GetMapping("/layout/AdBlog")
    public String editdata(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "s", required = false) String search, Model model) {
        Page<OgnBlog> dataBlog = BlogService.findAll(search, pageNumber);
        // Tính toán trang bắt đầu và kết thúc để hiển thị chỉ 1 trang xung quanh trang hiện tại
        int totalPages = dataBlog.getTotalPages();
        int visiblePages = 1; // Số trang hiển thị xung quanh trang hiện tại

        // Tính toán startPage và endPage
        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Thêm các giá trị cần thiết vào model
        model.addAttribute("s", search);
        model.addAttribute("dataBlog", dataBlog);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "admin/layout/AdBlog";
    }

    // CRUD Product
    private static final String UPLOAD_DIR = "C:/PC/Java IJ/Project/ProjectWeb/src/main/resources/static/assets/images/blog";

    @PostMapping("/insertBlog")
    public String insertBlog(@RequestParam("image1") MultipartFile image1, OgnBlog blog, Model model) {
        try {
            // Kiểm tra xem file đã được chọn chưa
            if (image1.isEmpty()) {
                // Nếu không chọn ảnh mới, kiểm tra xem blog có ảnh cũ chưa
                if (blog.getImage() == null || blog.getImage().isEmpty()) {
                    model.addAttribute("error", "Vui lòng chọn ảnh.");
                    return "redirect:/admin/layout/AdBlog"; // Trả về lại trang nếu không chọn ảnh và không có ảnh cũ
                }
                // Nếu không có ảnh mới và đã có ảnh cũ, bỏ qua phần lưu ảnh
            } else {
                // Lấy tên tệp ảnh
                String fileName = image1.getOriginalFilename();
                // Đường dẫn đầy đủ đến thư mục lưu trữ ảnh
                File destinationDir = new File(UPLOAD_DIR);

                // Đường dẫn đến tệp ảnh
                File destinationFile = new File(destinationDir, fileName);

                // Kiểm tra xem tệp ảnh đã tồn tại trong thư mục chưa
                if (!destinationFile.exists()) {
                    // Nếu ảnh chưa tồn tại, tiến hành lưu ảnh mới
                    image1.transferTo(destinationFile); // Lưu tệp vào thư mục
                }

                // Lưu đường dẫn ảnh vào OgnBlog
                blog.setImage("/assets/images/blog/" + fileName);  // Lưu đường dẫn trong CSDL
            }

            // Cập nhật thời gian đăng blog
            blog.setBlogdate(Instant.now());

            // Thực hiện lưu blog vào cơ sở dữ liệu
            BlogService.insertBlog(blog);  // Phương thức insertBlog từ service

            // Hiển thị thông báo thành công
            model.addAttribute("message", "Thêm blog thành công!");

        } catch (IOException e) {
            model.addAttribute("error", "Có lỗi xảy ra khi lưu tệp ảnh.");
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi thêm blog.");
        }

        return "redirect:/admin/layout/AdBlog"; // Điều hướng về trang quản lý blog
    }




    // Phương thức cập nhật người dùng
    @PostMapping("/updateBlog")
    public String updateProduct(@RequestParam("images2") MultipartFile images2,@ModelAttribute OgnBlog blog, Model model) {
        try {
            // Kiểm tra xem file đã được chọn chưa
            if (images2.isEmpty()) {

            } else {
                // Lấy tên tệp ảnh
                String fileName = images2.getOriginalFilename();
                // Đường dẫn đầy đủ đến thư mục lưu trữ ảnh
                File destinationDir = new File(UPLOAD_DIR);

                // Đường dẫn đến tệp ảnh
                File destinationFile = new File(destinationDir, fileName);

                // Kiểm tra xem tệp ảnh đã tồn tại trong thư mục chưa
                if (!destinationFile.exists()) {
                    // Nếu ảnh chưa tồn tại, tiến hành lưu ảnh mới
                    images2.transferTo(destinationFile); // Lưu tệp vào thư mục
                }

                // Lưu đường dẫn ảnh vào OgnBlog
                blog.setImage("/assets/images/blog/" + fileName);  // Lưu đường dẫn trong CSDL
            }

            OgnBlog updatedBlog = BlogService.updateBlog(blog); // Gọi hàm update từ AdminService
            if (updatedBlog != null) {
                model.addAttribute("message", "Cập nhật sản phẩm thành công!");
            } else {
                model.addAttribute("error", "Sản phẩm không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        return "redirect:/admin/layout/AdBlog"; // Trả về trang hiển thị người dùng
    }

    // Phương thức xóa người dùng
    @GetMapping("/deleteBlog")
    public String deleteReview(@RequestParam("id") long id, Model model) {
        BlogService.deleteBlog(id);
        return "redirect:/admin/layout/AdBlog";
    }
    // CRUD Product

}
