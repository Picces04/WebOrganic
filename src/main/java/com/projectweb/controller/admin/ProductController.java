package com.projectweb.controller.admin;

import com.projectweb.model.OgnProduct;
import com.projectweb.service.admin.CategoryService;
import com.projectweb.service.admin.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/layout/AdProduct")
    public String editProdata(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                              @RequestParam(value = "s", required = false) String search, Model model) {
        // Use Sort object for category name sorting
        Sort sort = Sort.by(Sort.Direction.ASC, "categoryid.categoryname");
        Page<OgnProduct> dataProduct = productService.findAll(search, pageNumber, sort);

        model.addAttribute("categoryList", categoryService.findAll());
        // Calculate page details
        int totalPages = dataProduct.getTotalPages();
        int visiblePages = 1;

        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Add data to model
        model.addAttribute("s", search);
        model.addAttribute("dataProduct", dataProduct);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "admin/layout/AdProduct";
    }

    // CRUD Product

    private static final String UPLOAD_DIR = "C:/PC/Java IJ/ProjectWeb/src/main/resources/static/assets/images/product";

    @PostMapping("/insertProduct")
    public String insertProduct(@RequestParam("image1") MultipartFile image1,
                                @RequestParam("image2") MultipartFile image2,
                                @RequestParam("image3") MultipartFile image3,
                                @RequestParam("image4") MultipartFile image4,
                                @RequestParam("image5") MultipartFile image5,OgnProduct product, Model model) {
        try {
            // Kiểm tra xem file đã được chọn chưa
            if (image1.isEmpty()) {
                // Nếu không chọn ảnh mới, kiểm tra xem blog có ảnh cũ chưa
                if (product.getImage() == null || product.getImage().isEmpty()) {
                    model.addAttribute("error", "Vui lòng chọn ảnh.");
                    return "redirect:/admin/layout/AdProduct"; // Trả về lại trang nếu không chọn ảnh và không có ảnh cũ
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
                product.setImage("/assets/images/product/" + fileName);  // Lưu đường dẫn trong CSDL

                if (!image2.isEmpty() || !image3.isEmpty() || !image4.isEmpty() || !image5.isEmpty()) {
                    List<String> filePaths = new ArrayList<>();

                    if (!image2.isEmpty()) {
                        String fileName2 = image2.getOriginalFilename();
                        File destinationFile2 = new File(UPLOAD_DIR, fileName2);
                        if (!destinationFile2.exists()) {
                            image2.transferTo(destinationFile2);
                        }
                        filePaths.add("/assets/images/product/" + fileName2);
                    }

                    if (!image3.isEmpty()) {
                        String fileName3 = image3.getOriginalFilename();
                        File destinationFile3 = new File(UPLOAD_DIR, fileName3);
                        if (!destinationFile3.exists()) {
                            image3.transferTo(destinationFile3);
                        }
                        filePaths.add("/assets/images/product/" + fileName3);
                    }

                    if (!image4.isEmpty()) {
                        String fileName4 = image4.getOriginalFilename();
                        File destinationFile4 = new File(UPLOAD_DIR, fileName4);
                        if (!destinationFile4.exists()) {
                            image4.transferTo(destinationFile4);
                        }
                        filePaths.add("/assets/images/product/" + fileName4);
                    }

                    if (!image5.isEmpty()) {
                        String fileName5 = image5.getOriginalFilename();
                        File destinationFile5 = new File(UPLOAD_DIR, fileName5);
                        if (!destinationFile5.exists()) {
                            image5.transferTo(destinationFile5);
                        }
                        filePaths.add("/assets/images/product/" + fileName5);
                    }

                    if (!filePaths.isEmpty()) {
                        product.setListimage(String.join(",", filePaths));
                    }
                }

            }

            productService.insertProduct(product); // Gọi hàm insert từ AdminService
            model.addAttribute("messageProduct", "Thêm sản phẩm thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorProduct", "Có lỗi xảy ra khi thêm admin!");
        }
        return "redirect:/admin/layout/AdProduct";
    }

    // Phương thức cập nhật người dùng
    @PostMapping("/updateProduct")
    public String updateProduct(@RequestParam("imagesUpdate") MultipartFile imagesUpdate,@ModelAttribute OgnProduct product, Model model) {
        try {
            // Kiểm tra xem file đã được chọn chưa
            if (imagesUpdate.isEmpty() && imagesUpdate != null && imagesUpdate.getOriginalFilename().equals("") ) {
            } else {
                // Lấy tên tệp ảnh
                String fileName = imagesUpdate.getOriginalFilename();
                // Đường dẫn đầy đủ đến thư mục lưu trữ ảnh
                File destinationDir = new File(UPLOAD_DIR);

                // Đường dẫn đến tệp ảnh
                File destinationFile = new File(destinationDir, fileName);

                // Kiểm tra xem tệp ảnh đã tồn tại trong thư mục chưa
                if (!destinationFile.exists()) {
                    // Nếu ảnh chưa tồn tại, tiến hành lưu ảnh mới
                    imagesUpdate.transferTo(destinationFile); // Lưu tệp vào thư mục
                }

                // Lưu đường dẫn ảnh vào OgnBlog
                product.setImage("/assets/images/product/" + fileName);  // Lưu đường dẫn trong CSDL
            }

            OgnProduct updatedProduct = productService.updateProduct(product); // Gọi hàm update từ AdminService
            if (updatedProduct != null) {
                model.addAttribute("messageProduct", "Cập nhật sản phẩm thành công!");
            } else {
                model.addAttribute("errorProduct", "Sản phẩm không tồn tại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
        }
        return "redirect:/admin/layout/AdProduct"; // Trả về trang hiển thị người dùng
    }

    // Phương thức xóa người dùng
    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("id") long id, RedirectAttributes redirectAttributes) {
        try {
            boolean isDeleted = productService.deleteProduct(id);
            if (isDeleted) {
                redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công!"); // Thêm thông báo vào flash attribute
            } else {
                redirectAttributes.addFlashAttribute("error", "Sản phẩm đang giảm giá!"); // Thêm thông báo lỗi vào flash attribute
            }
        }catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa sản phẩm!");
        }

        return "redirect:/admin/layout/AdProduct";
    }
    // CRUD Product

}
