package com.projectweb.controller.home;

import com.projectweb.model.OgnBlog;
import com.projectweb.model.OgnStarrating;
import com.projectweb.model.OgnUser;
import com.projectweb.model.dto.CartDTO;
import com.projectweb.model.dto.HeaderDTO;
import com.projectweb.service.home.BlogHomeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home/user")
public class BlogHomeDetailsCtrl {

    @Autowired
    private BlogHomeService blogHomeService;

    @GetMapping("/blog-details/{id}")
    public String shop(@PathVariable("id") Long id, HttpSession session,
                       Model model) {
        //Lọc theo giá
        // Gọi service để lấy blog
        OgnBlog OneBlog = blogHomeService.findOne(id);
        model.addAttribute("OneBlog", OneBlog);

        String tagPick = OneBlog.getTag();
        // Lọc các blog liên quan theo tag
        List<OgnBlog> Allblog  = blogHomeService.findAll();
        List<OgnBlog> relatedBlog = Allblog.stream()
                .filter(r -> String.valueOf(tagPick).equals(r.getTag())) // Lọc các phần tử có id khớp
                .limit(3)
                .collect(Collectors.toList()); // Thu thập thành danh sách
        model.addAttribute("relatedBlog", relatedBlog);

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

        return "home/user/blog-details"; // Trả về chuỗi đường dẫn đến trang HTML
    }

}
