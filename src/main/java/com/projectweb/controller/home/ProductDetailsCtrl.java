package com.projectweb.controller.home;

import com.projectweb.model.*;
import com.projectweb.model.dto.CartDTO;
import com.projectweb.model.dto.HeaderDTO;
import com.projectweb.model.dto.ProductDTOSale;
import com.projectweb.service.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class ProductDetailsCtrl {

    @Autowired
    private UserService userService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SlideService slideService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/user/product-details")
    public String productdetails(@RequestParam("id") Long id, HttpSession session, Model model) {
        // Lấy review
        List<OgnStarrating> dataReview = reviewService.findStatus();
        model.addAttribute("dataReview", dataReview);

        // Lọc ra tất cả các phần tử có id khớp với id được truyền vào
        List<OgnStarrating> reviewsByProduct = dataReview.stream()
                .filter(r -> Long.valueOf(id).equals(r.getProductid().getId())) // Lọc các phần tử có id khớp
                .collect(Collectors.toList()); // Thu thập thành danh sách
        model.addAttribute("reviewsByProduct", reviewsByProduct);

        //Lấy review
        if (reviewsByProduct != null && !reviewsByProduct.isEmpty()) {
            // Tính tổng với kiểu Long
            long sumStar = reviewsByProduct.stream()
                    .mapToLong(OgnStarrating::getRating)
                    .sum();
            // Tính trung bình
            double avgStar = (double) sumStar / reviewsByProduct.size();

            // Làm tròn và đảm bảo giá trị nằm trong phạm vi [1, 5]
            int roundedStar = (int) Math.round(avgStar);
            roundedStar = Math.max(1, Math.min(roundedStar, 5));

            model.addAttribute("AbsStar", roundedStar);
        } else {
            model.addAttribute("AbsStar", 5); // Giá trị mặc định nếu không có đánh giá
        }

        // Kiểm tra xem có đánh giá nào có email trùng với người dùng đang đăng nhập không
        // Lấy thông tin người dùng từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName(); // Email người dùng đăng nhập

        boolean showMessage = reviewsByProduct.stream()
                .noneMatch(review -> review.getUserid().getEmail().equals(loggedInEmail));

        model.addAttribute("showMessage", showMessage);  // Thêm biến kiểm tra

        OgnUser userPick = userService.findByEmail(loggedInEmail);
        model.addAttribute("userPick", userPick);
        //Lấy review


        //Lấy quảng cáo home
        List<OgnSlide> dataSlideTop = slideService.findStatusTop();
        model.addAttribute("dataSlideTop", dataSlideTop);

        List<OgnSlide> dataSlideMid = slideService.findStatusMid();
        model.addAttribute("dataSlideMid", dataSlideMid);

        List<OgnSlide> dataSlideBottom = slideService.findStatusBottom();
        model.addAttribute("dataSlideBottom", dataSlideBottom);
        //Lấy quảng cáo home

        //Lấy sale
        List<OgnSaleprice> dataSale = saleService.findAll();
        model.addAttribute("dataSale", dataSale);

        OgnSaleprice saleByProduct = dataSale.stream()
                .filter(r -> Long.valueOf(id).equals(r.getProductid().getId())) // Lọc phần tử có id khớp
                .findFirst() // Lấy phần tử đầu tiên nếu có
                .orElse(null); // Trả về null nếu không có phần tử nào khớp
        model.addAttribute("saleByProduct", saleByProduct);

        List<OgnSaleprice> dataSaleTomorrow = saleService.findSaleTheDay();
        model.addAttribute("dataSaleTomorrow", dataSaleTomorrow);
        //Lấy sale

        // Lấy tất cả các sản phẩm
        List<OgnProduct> dataProduct = productService.findAll();
        model.addAttribute("dataProduct", dataProduct);

        // Lấy sản phẩm khi click
        OgnProduct Oneproduct = productService.findOne(id);
        model.addAttribute("Oneproduct", Oneproduct);

        // Lấy sản phẩm bao gồm giảm giá khi click
            ProductDTOSale dto = new ProductDTOSale();
            dto.setId(Oneproduct.getId());
            dto.setCategoryId(Oneproduct.getCategoryid().getId());
            dto.setProductname(Oneproduct.getProductname());
            dto.setShortdesc(Oneproduct.getShortdesc());
            dto.setPrice(Oneproduct.getPrice());
            dto.setImage(Oneproduct.getImage());
            dto.setStock(Oneproduct.getStock());

            // Tìm sale percentage cho sản phẩm từ dataSale
            BigDecimal salePercentage = null;
            for (OgnSaleprice sale : dataSale) {
                if (sale.getProductid().getId().equals(Oneproduct.getId())) { // Kiểm tra nếu productId khớp
                    salePercentage = sale.getSalepercentage();
                    dto.setSalePrice(salePercentage);
                    break; // Dừng tìm kiếm khi tìm thấy sale percentage
                }else {
                    dto.setSalePrice(BigDecimal.valueOf(1));
                }
            }
        model.addAttribute("productDTOList", dto);

        // Lấy sản phẩm đã xem từ session
        List<ProductDTOSale> recentlyViewed = (List<ProductDTOSale>) session.getAttribute("recentlyViewed");
        if (recentlyViewed == null) {
            recentlyViewed = new ArrayList<>();
        }

        // Kiểm tra và thêm sản phẩm vào danh sách đã xem
        boolean productExists = false;
        for (ProductDTOSale dtoSale : recentlyViewed) {
            if (dtoSale.getId().equals(dto.getId())) {
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            if (recentlyViewed.size() >= 10) {
                recentlyViewed.remove(0); // Nếu đã có 10 sản phẩm, loại bỏ sản phẩm cũ nhất
            }
            recentlyViewed.add(dto); // Thêm sản phẩm mới vào danh sách
        }

        // Lưu lại danh sách đã xem vào session
        session.setAttribute("recentlyViewed", recentlyViewed);
        model.addAttribute("recentlyViewed", recentlyViewed);
        // Lấy tất cả các sản phẩm

        Long idbyOneproduct = Oneproduct.getCategoryid().getId();

        //Lấy sản phẩm liên quan
        List<OgnSaleprice> RelatedSaleProduct = dataSale.stream()
                .filter(r -> Long.valueOf(idbyOneproduct).equals(r.getCategoryid().getId())) // Lọc các phần tử có id khớp
                .collect(Collectors.toList()); // Thu thập thành danh sách
        model.addAttribute("RelatedSaleProduct", RelatedSaleProduct);
        // Lấy sản phẩm khi click


        // Lấy list ảnh
        // Tách listimage thành danh sách các đường dẫn
        if (Oneproduct.getListimage() != null) {
            List<String> imageList = Arrays.stream(Oneproduct.getListimage().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            model.addAttribute("imageList", imageList);
        }
        // Lấy list ảnh

        //Lấy session cho giỏ hàng
        List<CartDTO> cartList1 = (List<CartDTO>) session.getAttribute("cartList");
        model.addAttribute("cartList1", cartList1);

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
        OgnUser UserGitSS = (OgnUser) session.getAttribute("UserGitSS");
        model.addAttribute("UserGitSS", UserGitSS);

        return "home/user/product-details"; // Trả về chuỗi đường dẫn đến trang HTML
    }

    @PostMapping("/insertReviewHome")
    public String insertReview(OgnStarrating review,@RequestHeader(value = "referer", required = false) String referer,
                               Model model) {
        try {
            review.setRatingdate(Instant.now());
            review.setStatus("Home");
            reviewService.insertReview(review); // Gọi hàm insert từ AdminService
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:" + (referer != null ? referer : "/home/user/index");
    }
}
