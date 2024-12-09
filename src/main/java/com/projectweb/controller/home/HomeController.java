package com.projectweb.controller.home;

import com.projectweb.model.*;
import com.projectweb.model.dto.CartDTO;
import com.projectweb.model.dto.HeaderDTO;
import com.projectweb.model.dto.ProductDTOSale;
import com.projectweb.service.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home/user")
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SlideService slideService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model, HttpSession session) {
        //Lấy review
        List<OgnStarrating> dataReview = reviewService.findStatus();
        model.addAttribute("dataReview", dataReview);

        //Lấy quảng cáo home
        List<OgnSlide> dataSlideTop = slideService.findStatusTop();
        model.addAttribute("dataSlideTop", dataSlideTop);

        List<OgnSlide> dataSlideMid = slideService.findStatusMid();
        model.addAttribute("dataSlideMid", dataSlideMid);

        List<OgnSlide> dataSlideBottom = slideService.findStatusBottom();
        model.addAttribute("dataSlideBottom", dataSlideBottom);

        //Lấy danh mục home
        List<OgnCategory> dataCategory = categoryService.findStatus();
        model.addAttribute("dataCategory", dataCategory);

        // Lấy tất cả các danh mục
        List<OgnCategory> dataCategoryAll = categoryService.findAll();
        model.addAttribute("dataCategoryAll", dataCategoryAll);
        //Lấy 3 danh mục
        List<OgnCategory> dataCategory3 = dataCategoryAll.stream().limit(3).toList();
        model.addAttribute("dataCategory3", dataCategory3);

        //Lấy sale
        List<OgnSaleprice> dataSale = saleService.findAll();
        model.addAttribute("dataSale", dataSale);

        List<OgnSaleprice> dataSaleTomorrow = saleService.findSaleTheDay();
        model.addAttribute("dataSaleTomorrow", dataSaleTomorrow);

        // Lấy tất cả các sản phẩm
        List<OgnProduct> dataProduct = productService.findAll();

        List<ProductDTOSale> productDTOList = new ArrayList<>();
        for (OgnProduct product : dataProduct) {
            ProductDTOSale dto = new ProductDTOSale();
            dto.setId(product.getId());
            dto.setCategoryId(product.getCategoryid().getId());
            dto.setProductname(product.getProductname());
            dto.setShortdesc(product.getShortdesc());
            dto.setPrice(product.getPrice());
            dto.setImage(product.getImage());
            dto.setStock(product.getStock());

            // Tìm sale percentage cho sản phẩm từ dataSale
            BigDecimal salePercentage = null;
            for (OgnSaleprice sale : dataSale) {
                if (sale.getProductid().getId().equals(product.getId())) { // Kiểm tra nếu productId khớp
                    salePercentage = sale.getSalepercentage();
                    dto.setSalePrice(salePercentage);
                    break; // Dừng tìm kiếm khi tìm thấy sale percentage
                }else {
                    dto.setSalePrice(BigDecimal.valueOf(1));
                }
            }
            productDTOList.add(dto);
        }
        model.addAttribute("productDTOList", productDTOList);

        // Nhóm các sản phẩm theo categoryid
        Map<Long, List<OgnProduct>> productsByCategory = new HashMap<>();
        Map<Long, List<ProductDTOSale>> productDTOsale = new HashMap<>();
        Map<Long, Integer> productCountByCategory = new HashMap<>();
        for (OgnCategory category : dataCategoryAll) {
            // Lọc các sản phẩm theo categoryid
            List<OgnProduct> productsInCategory = dataProduct.stream()
                    .filter(product -> product.getCategoryid().getId().equals(category.getId()))
                    .collect(Collectors.toList());
            List<ProductDTOSale> productsDTOInCategory = productDTOList.stream()
                    .filter(product -> product.getCategoryId().equals(category.getId()))
                    .collect(Collectors.toList());
            productsByCategory.put(category.getId(), productsInCategory);
            productCountByCategory.put(category.getId(), productsInCategory.size());
            productDTOsale.put(category.getId(), productsDTOInCategory);
        }

        // Thêm vào model để truy cập từ trang HTML
        model.addAttribute("productsByCategory", productsByCategory);
        model.addAttribute("productCountByCategory", productCountByCategory);
        model.addAttribute("productDTOsale", productDTOsale);

        // Lấy danh sách sản phẩm đã xem từ session
        List<ProductDTOSale> recentlyViewed = (List<ProductDTOSale>) session.getAttribute("recentlyViewed");
        model.addAttribute("recentlyViewed", recentlyViewed);
        // Thêm vào model để hiển thị trên view

        //Lấy session cho giỏ hàng
        List<CartDTO> cartList1 = (List<CartDTO>) session.getAttribute("cartList");
        model.addAttribute("cartList1", cartList1);

        //Lấy tổng sản phẩm
        Long totalProduct = 0L;  // Khởi tạo totalProduct là Long
        BigDecimal sumPrice = BigDecimal.valueOf(0);  // Khởi tạo sumPrice là BigDecimal

        if (cartList1 != null && !cartList1.isEmpty()) {
            for (CartDTO cartDTO : cartList1) {
                Long quantityItem = cartDTO.getQuantity(); // Lấy số lượng sản phẩm
                totalProduct += quantityItem; // Cộng tổng số sản phẩm

                // Chuyển giá và salePrice thành BigDecimal và tính tổng giá sản phẩm
                BigDecimal price = cartDTO.getPrice();
                BigDecimal salePrice = cartDTO.getSalePrice();
                BigDecimal sumItemPrice = price.multiply(salePrice); // Giá sản phẩm sau salePrice

                // Tính tổng giá sản phẩm
                sumPrice = sumPrice.add(sumItemPrice.multiply(BigDecimal.valueOf(quantityItem)));
            }

            HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
            if (headerDTO == null) {
                headerDTO = new HeaderDTO(); // Khởi tạo đối tượng mới
            }
            headerDTO.setTotalProduct(totalProduct);
            headerDTO.setSumPrice(sumPrice);

            session.setAttribute("headerDTO", headerDTO);
            model.addAttribute("headerDTO", headerDTO);
        }

        //Lấy session cho giỏ hàng

        HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
        model.addAttribute("headerDTO", headerDTO);

        HeaderDTO totalWish = (HeaderDTO) session.getAttribute("totalWish");
        model.addAttribute("totalWish", totalWish);

        List<Long> wishlistIds = new ArrayList<>();
        List<CartDTO> wishlist = (List<CartDTO>) session.getAttribute("wishlist");
        model.addAttribute("wishlist", wishlist);
        if (wishlist != null) {
            wishlistIds = wishlist.stream().map(CartDTO::getId).collect(Collectors.toList());
        }

        // Truyền danh sách ID của sản phẩm trong wishlist vào model
        model.addAttribute("wishlistIds", wishlistIds);

        //Lấy session cho giỏ hàng

        //Lấy session git
        if (principal != null) {
            String emailGit = principal.getAttribute("email");
            OgnUser UserGit = userService.findByEmail(emailGit);
            OgnUser UserGitSS = (OgnUser) session.getAttribute("UserGitSS");
            if (UserGitSS == null) {
                UserGitSS = new OgnUser() {
                }; // Khởi tạo đối tượng mới
            }
            if (UserGitSS != null) {
                UserGitSS.setUserid(UserGit.getUserid()); // Ví dụ với trường 'name'
                UserGitSS.setEmail(UserGit.getEmail());
                UserGitSS.setUsername(UserGit.getUsername());
                UserGitSS.setPasswordhash(UserGit.getPasswordhash());
                UserGitSS.setRole(UserGit.getRole());
                UserGitSS.setCreateddate(UserGit.getCreateddate());
                UserGitSS.setPhone(UserGit.getPhone());
                // Thêm các trường khác tùy thuộc vào cấu trúc của OgnUser
            }

            session.setAttribute("UserGitSS", UserGitSS);
            model.addAttribute("UserGitSS", UserGitSS);
        }

        //Lấy session git
        OgnUser UserGitSS = (OgnUser) session.getAttribute("UserGitSS");
        model.addAttribute("UserGitSS", UserGitSS);

        return "home/user/index"; // Trả về chuỗi đường dẫn đến trang HTML
    }



    @GetMapping("/about-us")
    public String aboutus(HttpSession session, Model model) {
        //Lấy session cho giỏ hàng
        List<CartDTO> cartList1 = (List<CartDTO>) session.getAttribute("cartList");
        model.addAttribute("cartList1", cartList1);

        HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
        model.addAttribute("headerDTO", headerDTO);

        HeaderDTO totalWish = (HeaderDTO) session.getAttribute("totalWish");
        model.addAttribute("totalWish", totalWish);
        //Lấy session cho giỏ hàng
        return "home/user/about-us";
    }
    @GetMapping("/contact-us")
    public String contactus(HttpSession session, Model model) {
        //Lấy session cho giỏ hàng
        List<CartDTO> cartList1 = (List<CartDTO>) session.getAttribute("cartList");
        model.addAttribute("cartList1", cartList1);

        HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
        model.addAttribute("headerDTO", headerDTO);

        HeaderDTO totalWish = (HeaderDTO) session.getAttribute("totalWish");
        model.addAttribute("totalWish", totalWish);


        //Lấy session cho giỏ hàng
        return "home/user/contact-us";
    }

}
