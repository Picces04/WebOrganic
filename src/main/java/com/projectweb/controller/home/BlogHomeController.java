package com.projectweb.controller.home;

import com.projectweb.model.*;
import com.projectweb.model.dto.CartDTO;
import com.projectweb.model.dto.HeaderDTO;
import com.projectweb.model.dto.ProductDTOSale;
import com.projectweb.service.admin.CategoryService;
import com.projectweb.service.admin.ProductService;
import com.projectweb.service.admin.SaleService;
import com.projectweb.service.admin.SlideService;
import com.projectweb.service.home.BlogHomeService;
import com.projectweb.service.home.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home/user")
public class BlogHomeController {

    @Autowired
    private BlogHomeService blogHomeService;

    @GetMapping("/blog")
    public String shop(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(value = "s", required = false) String search,
                       @RequestParam(value = "tag", required = false) String tag,
                       HttpSession session,
                       Model model) {
        //Lọc theo giá
        // Gọi service để lấy blog
        Page<OgnBlog> blogs = blogHomeService.findAll(pageNumber, search, tag);

        Page<OgnBlog> blogsAll = blogHomeService.findAll(pageNumber, null, null);

        // Lấy 3 blog đầu tiên từ blogs (getContent() trả về danh sách các phần tử)
        List<OgnBlog> topBlogs = blogsAll.getContent().stream()
                .limit(3) // Lấy 3 blog đầu tiên
                .collect(Collectors.toList());

        // Lọc các tag duy nhất từ Page<OgnBlog>
        Set<String> uniqueTags = new HashSet<>();
        for (OgnBlog blog : blogsAll.getContent()) {
            if (blog.getTag() != null) {
                uniqueTags.add(blog.getTag());  // Thêm tag vào Set (Set sẽ tự động loại bỏ trùng)
            }
        }

        // Truyền blogs và danh sách tag duy nhất vào model
        model.addAttribute("uniqueTags", uniqueTags);

        // Thêm topBlogs vào model để hiển thị trong Thymeleaf
        model.addAttribute("topBlogs", topBlogs);

        //Phân trang
        // Tính toán trang bắt đầu và kết thúc để hiển thị chỉ 3 trang xung quanh trang hiện tại
        int totalPages = blogs.getTotalPages();
        int visiblePages = 1; // Số trang hiển thị xung quanh trang hiện tại

        // Tính toán startPage và endPage
        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Thêm các giá trị cần thiết vào model
        model.addAttribute("blogs", blogs.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("s", search);
        //Phân trang

        //Lấy session cho giỏ hàng
        List<CartDTO> cartList1 = (List<CartDTO>) session.getAttribute("cartList");
        model.addAttribute("cartList1", cartList1);

        HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
        model.addAttribute("headerDTO", headerDTO);

        HeaderDTO totalWish = (HeaderDTO) session.getAttribute("totalWish");
        model.addAttribute("totalWish", totalWish);
        //Lấy session cho giỏ hàng

        //Lấy session git
        OgnUser UserGitSS = (OgnUser) session.getAttribute("UserGitSS");
        model.addAttribute("UserGitSS", UserGitSS);

        return "home/user/blog"; // Trả về chuỗi đường dẫn đến trang HTML
    }

}
