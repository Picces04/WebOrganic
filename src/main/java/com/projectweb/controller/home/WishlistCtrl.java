package com.projectweb.controller.home;


import com.projectweb.model.OgnProduct;
import com.projectweb.model.OgnSaleprice;
import com.projectweb.model.OgnUser;
import com.projectweb.model.dto.CartDTO;
import com.projectweb.model.dto.HeaderDTO;
import com.projectweb.service.admin.ProductService;
import com.projectweb.service.admin.SaleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home/user")
public class WishlistCtrl {
    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    @GetMapping("/wishlist")
    public String wishlist(HttpSession session, Model model) {
        //Lấy session cho giỏ hàng
        List<CartDTO> cartList1 = (List<CartDTO>) session.getAttribute("cartList");
        model.addAttribute("cartList1", cartList1);

        HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
        model.addAttribute("headerDTO", headerDTO);

        List<CartDTO> wishlist = (List<CartDTO>) session.getAttribute("wishlist");
        model.addAttribute("wishlist", wishlist);

        HeaderDTO totalWish = (HeaderDTO) session.getAttribute("totalWish");
        model.addAttribute("totalWish", totalWish);
        //Lấy session cho giỏ hàng
        return "home/user/wishlist";
    }

    @GetMapping("/addtowishlist")
    public String Addtocart(@RequestParam(value = "id", required = false) Long id,
                            @RequestHeader(value = "referer", required = false) String referer,
                            HttpSession session, Model model) {
        //Lấy sale
        List<OgnSaleprice> dataSale = saleService.findAll();
        model.addAttribute("dataSale", dataSale);

        // Lấy tất cả các sản phẩm
        List<OgnProduct> dataProduct = productService.findAll();
        model.addAttribute("dataProduct", dataProduct);

        // Lấy sản phẩm khi click
        OgnProduct Oneproduct = productService.findOne(id);
        model.addAttribute("Oneproduct", Oneproduct);


        CartDTO cart = new CartDTO();
        cart.setId(Oneproduct.getId());
        cart.setImage(Oneproduct.getImage());
        cart.setPrice(Oneproduct.getPrice());
        cart.setProductname(Oneproduct.getProductname());
        cart.setQuantity(Oneproduct.getStock());

        // Tìm sale percentage cho sản phẩm từ dataSale
        BigDecimal salePercentage = null;
        for (OgnSaleprice sale : dataSale) {
            if (sale.getProductid().getId().equals(Oneproduct.getId())) { // Kiểm tra nếu productId khớp
                salePercentage = sale.getSalepercentage();
                cart.setSalePrice(salePercentage);
                break; // Dừng tìm kiếm khi tìm thấy sale percentage
            }else {
                cart.setSalePrice(BigDecimal.valueOf(1));
            }
        }
        model.addAttribute("cart", cart);

        // Lấy sản phẩm đã xem từ session
        List<CartDTO> wishlist = (List<CartDTO>) session.getAttribute("wishlist");
        if (wishlist == null) {
            wishlist = new ArrayList<>();
        }

        // Kiểm tra và thêm sản phẩm vào danh sách đã xem
        boolean productExists = false;
        for (CartDTO cartDTO : wishlist) {
            if (cartDTO.getId() == Oneproduct.getId()) {
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            wishlist.add(cart); // Thêm sản phẩm mới vào danh sách
        }

        // Lưu lại danh sách đã xem vào session
        session.setAttribute("wishlist", wishlist);
        model.addAttribute("wishlist", wishlist);

        //Lấy session cho giỏ hàng

        //Lấy tổng sản phẩm
        Long totalProduct = 0L;  // Khởi tạo totalProduct là Long
        BigDecimal sumPrice = BigDecimal.valueOf(0);  // Khởi tạo sumPrice là BigDecimal

        if (wishlist != null && !wishlist.isEmpty()) {
            for (CartDTO cartDTO : wishlist) {
                totalProduct += 1; // Cộng tổng số sản phẩm
            }

            HeaderDTO totalWish = (HeaderDTO) session.getAttribute("totalWish");
            if (totalWish == null) {
                totalWish = new HeaderDTO(); // Khởi tạo đối tượng mới
            }
            totalWish.setTotalProduct(totalProduct);

            session.setAttribute("totalWish", totalWish);
            model.addAttribute("totalWish", totalWish);
        }
        //Lấy session cho giỏ hàng

        //Lấy session git
        OgnUser UserGitSS = (OgnUser) session.getAttribute("UserGitSS");
        model.addAttribute("UserGitSS", UserGitSS);

        // Chuyển hướng về trang trước đó hoặc mặc định về index nếu không có referer
        return "redirect:" + (referer != null ? referer : "/home/user/index");
    }

    @PostMapping("/remove-wishlist-item")
    @ResponseBody
    public Map<String, Object> removeWishListItem(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long productId = payload.get("id") != null ? Long.parseLong(payload.get("id").toString()) : null;

            if (productId == null) {
                response.put("success", false);
                response.put("message", "Invalid product ID");
                return response;
            }

            // Lấy wishlist từ session
            List<CartDTO> wishlist = (List<CartDTO>) session.getAttribute("wishlist");
            if (wishlist != null) {
                wishlist.removeIf(wishListItem -> wishListItem.getId() == productId); // Xóa sản phẩm khỏi wishlist
                session.setAttribute("wishlist", wishlist); // Cập nhật lại session
            }

            response.put("success", true);
            response.put("message", "Product removed from wishlist successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
        }
        return response;
    }

}

