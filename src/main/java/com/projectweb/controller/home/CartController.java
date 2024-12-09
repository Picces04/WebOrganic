package com.projectweb.controller.home;

import com.projectweb.model.OgnCart;
import com.projectweb.model.OgnProduct;
import com.projectweb.model.OgnSaleprice;
import com.projectweb.model.OgnUser;
import com.projectweb.model.dto.CartDTO;
import com.projectweb.model.dto.HeaderDTO;
import com.projectweb.model.dto.ProductDTOSale;
import com.projectweb.service.admin.ProductService;
import com.projectweb.service.admin.SaleService;
import com.projectweb.service.home.BlogHomeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    @GetMapping("/addtocart")
    public String Addtocart(@RequestParam(value = "id", required = false) Long id,
                            @RequestParam(value = "quantity", required = false) Long quantity,
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

        // Lấy sản phẩm bao gồm giảm giá khi click
        Long Itemquantity1 = quantity;

        CartDTO cart = new CartDTO();
        cart.setId(Oneproduct.getId());
        cart.setImage(Oneproduct.getImage());
        cart.setPrice(Oneproduct.getPrice());
        cart.setProductname(Oneproduct.getProductname());
        cart.setStock(Oneproduct.getStock());
        if (Itemquantity1 != null) {
            cart.setQuantity(Itemquantity1);
        }else {
            cart.setQuantity(Long.valueOf(1));
        }
        
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
        List<CartDTO> cartList = (List<CartDTO>) session.getAttribute("cartList");
        if (cartList == null) {
            cartList = new ArrayList<>();
        }

        // Kiểm tra và thêm sản phẩm vào danh sách đã xem
        boolean productExists = false;
        for (CartDTO cartDTO : cartList) {
            if (cartDTO.getId() == Oneproduct.getId()) {
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            cartList.add(cart); // Thêm sản phẩm mới vào danh sách
        } else {
            // Tìm sản phẩm đã tồn tại và tăng quantity
            Long ItemQuantity = quantity;
            if (quantity == null) {
                ItemQuantity = Long.valueOf(1);
            }

            for (CartDTO item : cartList) {
                if (item.getId() == (cart.getId())) {
                    item.setQuantity(item.getQuantity() + ItemQuantity );
                    break; // Dừng vòng lặp sau khi tìm thấy sản phẩm
                }
            }
        }

        // Lưu lại danh sách đã xem vào session
        session.setAttribute("cartList", cartList);
        model.addAttribute("cartList", cartList);


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

        // Chuyển hướng về trang trước đó hoặc mặc định về index nếu không có referer
        return "redirect:" + (referer != null ? referer : "/home/user/index");
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {

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
        
        return "home/user/cart"; // Trả về chuỗi đường dẫn đến trang HTML
    }

    @PostMapping("/update-cart")
    @ResponseBody
    public Map<String, Object> updateCart(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long productId = Long.parseLong(payload.get("id").toString()) ;
            Long updatedQuantity = Long.parseLong(payload.get("quantity").toString()) ;

            // Lấy giỏ hàng từ session
            List<CartDTO> cartList = (List<CartDTO>) session.getAttribute("cartList");
            if (cartList != null) {
                for (CartDTO cartItem : cartList) {
                    if (cartItem.getId()== productId) {
                        cartItem.setQuantity(updatedQuantity);
                        break;
                    }
                }
                // Cập nhật lại session
                session.setAttribute("cartList", cartList);

                Long totalProduct = 0L;  // Khởi tạo totalProduct là Long
                BigDecimal sumPrice = BigDecimal.valueOf(0);  // Khởi tạo sumPrice là BigDecimal
                if (cartList != null && !cartList.isEmpty()) {
                    for (CartDTO cartDTO : cartList) {
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
                }
            }

            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/remove-cart-item")
    @ResponseBody
    public Map<String, Object> removeCartItem(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long productId = payload.get("id") != null ? Long.parseLong(payload.get("id").toString()) : null;

            if (productId == null) {
                response.put("success", false);
                response.put("message", "Invalid product ID");
                return response;
            }

            // Lấy giỏ hàng từ session
            List<CartDTO> cartList = (List<CartDTO>) session.getAttribute("cartList");
            if (cartList != null) {
                cartList.removeIf(cartItem -> cartItem.getId()==productId); // Xóa sản phẩm khỏi giỏ hàng
                session.setAttribute("cartList", cartList); // Cập nhật lại session
            }

            Long totalProduct = 0L;  // Khởi tạo totalProduct là Long
            BigDecimal sumPrice = BigDecimal.valueOf(0);  // Khởi tạo sumPrice là BigDecimal
            if (cartList != null && !cartList.isEmpty()) {
                for (CartDTO cartDTO : cartList) {
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
            }else {
                HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
                if (headerDTO == null) {
                    headerDTO = new HeaderDTO(); // Khởi tạo đối tượng mới
                }
                headerDTO.setTotalProduct(0);
                headerDTO.setSumPrice(BigDecimal.valueOf(0));

                session.setAttribute("headerDTO", headerDTO);
            }

            response.put("success", true);
            response.put("message", "Product removed successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/remove-all-cart-items")
    @ResponseBody
    public Map<String, Object> removeAllCartItems(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Lấy giỏ hàng từ session
            List<CartDTO> cartList = (List<CartDTO>) session.getAttribute("cartList");

            if (cartList != null) {
                cartList.clear(); // Xóa tất cả sản phẩm khỏi giỏ hàng
                session.setAttribute("cartList", cartList); // Cập nhật lại session
            }
            HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
            if (headerDTO == null) {
                headerDTO = new HeaderDTO(); // Khởi tạo đối tượng mới
            }
            headerDTO.setTotalProduct(0);
            headerDTO.setSumPrice(BigDecimal.valueOf(0));

            session.setAttribute("headerDTO", headerDTO);

            response.put("success", true);
            response.put("message", "All products removed successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
        }
        return response;
    }



}
