package com.projectweb.controller.admin;

import com.projectweb.model.OgnSlide;
import com.projectweb.service.admin.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class SlideController {

    @Autowired
    private SlideService slideService;

    @GetMapping("/layout/AdSlide")
    public String editdata(Model model) {
        List<OgnSlide> dataSlide = slideService.findAll();
        model.addAttribute("dataSlide", dataSlide);
        return "admin/layout/AdSlide";
    }


    // CRUD Category

    private static final String UPLOAD_DIR = "C:/PC/Java IJ/Project/ProjectWeb/src/main/resources/static/assets/images/slider";
    @PostMapping("/insertSlide")
    public String insertSlide(@RequestParam("imageurl1") MultipartFile imageurl1, OgnSlide slide, Model model) {
        try {
            // Kiểm tra xem file đã được chọn chưa
            if (imageurl1.isEmpty()) {
                // Nếu không chọn ảnh mới, kiểm tra xem blog có ảnh cũ chưa
                if (slide.getImageurl() == null || slide.getImageurl().isEmpty()) {
                    model.addAttribute("error", "Vui lòng chọn ảnh.");
                    return "redirect:/admin/layout/AdSlide"; // Trả về lại trang nếu không chọn ảnh và không có ảnh cũ
                }
                // Nếu không có ảnh mới và đã có ảnh cũ, bỏ qua phần lưu ảnh
            } else {
                // Lấy tên tệp ảnh
                String fileName = imageurl1.getOriginalFilename();
                // Đường dẫn đầy đủ đến thư mục lưu trữ ảnh
                File destinationDir = new File(UPLOAD_DIR);

                // Đường dẫn đến tệp ảnh
                File destinationFile = new File(destinationDir, fileName);

                // Kiểm tra xem tệp ảnh đã tồn tại trong thư mục chưa
                if (!destinationFile.exists()) {
                    // Nếu ảnh chưa tồn tại, tiến hành lưu ảnh mới
                    imageurl1.transferTo(destinationFile); // Lưu tệp vào thư mục
                }

                // Lưu đường dẫn ảnh vào OgnBlog
                slide.setImageurl("/assets/images/slider/" + fileName);  // Lưu đường dẫn trong CSDL
            }

            slideService.insertSlide(slide); // Gọi hàm insert từ AdminService
            model.addAttribute("messageSlide", "Thêm danh mục thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorSlide", "Có lỗi xảy ra khi thêm admin!");
        }
        return "redirect:/admin/layout/AdSlide";
    }

    // Phương thức cập nhật người dùng
    @PostMapping("/updateSlide")
    public String updateCategory(@RequestParam("images2") MultipartFile images2,@ModelAttribute OgnSlide slide, Model model) {
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
                slide.setImageurl("/assets/images/slider/" + fileName);  // Lưu đường dẫn trong CSDL
            }


            OgnSlide updatedSlide = slideService.updateSlide(slide); // Gọi hàm update từ AdminService
            if (updatedSlide != null) {
                model.addAttribute("messageSlide", "Cập nhật danh mục thành công!");
            } else {
                model.addAttribute("errorSlide", "Danh mục không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        return "redirect:/admin/layout/AdSlide"; // Trả về trang hiển thị người dùng
    }

    // Phương thức xóa người dùng
    @GetMapping("/deleteSlide")
    public String deleteCategory(@RequestParam("id") long id, Model model) {
        slideService.deleteSlide(id);
        return "redirect:/admin/layout/AdSlide";
    }
    // CRUD Category
}
