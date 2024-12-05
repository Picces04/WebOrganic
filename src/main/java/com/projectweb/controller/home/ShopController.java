package com.projectweb.controller.home;

import com.projectweb.model.*;
import com.projectweb.model.dto.CartDTO;
import com.projectweb.model.dto.HeaderDTO;
import com.projectweb.model.dto.ProductDTOSale;
import com.projectweb.service.admin.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home/user")
public class ShopController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SlideService slideService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/shop")
    public String shop(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(value = "minPrice", required = false) Double minPrice,
                       @RequestParam(value = "maxPrice", required = false) Double maxPrice,
                       @RequestParam(value = "s", required = false) String search,
                       @RequestParam(value = "sortby", required = false, defaultValue = "default") String sortby,
                       HttpSession session,
                       Model model) {
        //Lọc theo giá
        // Gọi service để lấy sản phẩm trong khoảng giá
        Page<OgnProduct> productPage = shopService.findAll(minPrice, maxPrice, pageNumber, sortby,search);

        //Phân trang
        // Tính toán trang bắt đầu và kết thúc để hiển thị chỉ 3 trang xung quanh trang hiện tại
        int totalPages = productPage.getTotalPages();
        int visiblePages = 1; // Số trang hiển thị xung quanh trang hiện tại

        // Tính toán startPage và endPage
        int startPage = Math.max(1, pageNumber - visiblePages);
        int endPage = Math.min(totalPages, pageNumber + visiblePages);

        // Thêm các giá trị cần thiết vào model
        model.addAttribute("productPage", productPage.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sortby", sortby);
        //Phân trang
        // Lấy review
        List<OgnStarrating> dataReview = reviewService.findStatus();
        model.addAttribute("dataReview", dataReview);

        Map<OgnProduct, Integer> averageRatings = reviewService.getAverageStarRatings();

        // Chuyển đổi kết quả sang định dạng trả về đơn giản hơn
        Map<Long, Integer> result = averageRatings.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getId(), // Lấy ID sản phẩm
                        Map.Entry::getValue
                ));
        // Gán kết quả vào model để hiển thị
        model.addAttribute("result", result);

        // Lấy tất cả các sản phẩm
        List<OgnProduct> dataProduct = productService.findAll();
        model.addAttribute("dataProduct", dataProduct);

        // Lọc ra tất cả các phần tử có id khớp với id được truyền vào

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

        //Lấy sale
        List<OgnSaleprice> dataSale = saleService.findAll();
        model.addAttribute("dataSale", dataSale);

        List<ProductDTOSale> productDTOList = new ArrayList<>();
        for (OgnProduct product : productPage) {
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

        List<OgnSaleprice> dataSaleTomorrow = saleService.findSaleTheDay();
        model.addAttribute("dataSaleTomorrow", dataSaleTomorrow);

        

        // Nhóm các sản phẩm theo categoryid và đếm số lượng sản phẩm
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

            // Lưu danh sách sản phẩm và số lượng sản phẩm trong từng danh mục
            productsByCategory.put(category.getId(), productsInCategory);
            productCountByCategory.put(category.getId(), productsInCategory.size());
            productDTOsale.put(category.getId(), productsDTOInCategory);
        }


        // Thêm vào model để truy cập từ trang HTML
        model.addAttribute("productsByCategory", productsByCategory);
        model.addAttribute("productCountByCategory", productCountByCategory);
        model.addAttribute("productDTOsale", productDTOsale);


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

        return "home/user/shop"; // Trả về chuỗi đường dẫn đến trang HTML
    }

}
