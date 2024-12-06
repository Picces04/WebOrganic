package com.projectweb.controller.home;

import com.projectweb.model.*;
import com.projectweb.model.dto.CartDTO;
import com.projectweb.model.dto.HeaderDTO;
import com.projectweb.service.admin.BillService;
import com.projectweb.service.admin.OrderDetailsService;
import com.projectweb.service.admin.OrderService;
import com.projectweb.service.admin.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.security.Signature;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/home/user")
public class CheckoutController {

    @Autowired
    private BillService billService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Principal principal, Model model) {
        //Lấy session git
        OgnUser UserGitSS = (OgnUser) session.getAttribute("UserGitSS");
        model.addAttribute("UserGitSS", UserGitSS);
        boolean check = false;
        if (UserGitSS == null) {
            check = true;
        }
        if (check){
            if (principal == null) { // Người dùng chưa đăng nhập
                return "redirect:/admin/security/ADauthentication-login";
            }
        }

        //Lấy session cho giỏ hàng
        List<CartDTO> cartList1 = (List<CartDTO>) session.getAttribute("cartList");
        model.addAttribute("cartList1", cartList1);

        HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
        model.addAttribute("headerDTO", headerDTO);

        HeaderDTO totalWish = (HeaderDTO) session.getAttribute("totalWish");
        model.addAttribute("totalWish", totalWish);

        OgnUser userPick = (OgnUser) session.getAttribute("userPick");
        model.addAttribute("userPick", userPick);
        //Lấy session cho giỏ hàng


        return "home/user/checkout";
    }

    @PostMapping("/insertBill")
    public String insertAdmin(OgnBillingDetail billingDetail, HttpSession session , Model model) {
        OgnUser userPick = (OgnUser) session.getAttribute("userPick");
        String email = userPick.getEmail();
        List<CartDTO> cartList1 = (List<CartDTO>) session.getAttribute("cartList");

        try {
            // Lưu chi tiết order
            //Lưu order
            OgnOrder order = new OgnOrder();
            order.setOrderdate(Instant.now());
            order.setStatus("Đang xử lý");
            order.setUserid(billingDetail.getUserid());
            // Lưu order và lấy đối tượng đã lưu (có id)
            order = orderService.insertOrder(order);
            //Lưu bill
            billingDetail.setOrderid(order);
            billService.insertBill(billingDetail); // Gọi hàm insert từ AdminService

            for(CartDTO cartDTO : cartList1 ) {
                Long stock = cartDTO.getQuantity();
                OgnProduct productPick = productService.findOne(cartDTO.getId());
                if (productPick.getStock() - stock >= 0) {

                    productPick.setStock(productPick.getStock() - stock);
                    productService.updateProduct(productPick);

                    OgnOrderdetail orderdetail = new OgnOrderdetail();
                    OgnProduct ognProduct = new OgnProduct();
                    ognProduct.setId(cartDTO.getId());

                    orderdetail.setOrderid(order); // Nếu setOrderid nhận đối tượng OgnOrder
                    orderdetail.setProductid(ognProduct);
                    orderdetail.setQuantity(cartDTO.getQuantity());

                    BigDecimal realPrice = cartDTO.getPrice() ;
                    BigDecimal sale =  cartDTO.getSalePrice();
                    Long quantity = cartDTO.getQuantity();
                    if (sale == null) {
                        sale = BigDecimal.ONE;
                    }
                    BigDecimal lastPrice = realPrice.multiply(sale).multiply(BigDecimal.valueOf(quantity));
                    orderdetail.setPrice(lastPrice);
                    orderDetailsService.insertOrderdetail(orderdetail); // Lưu chi tiết order

                    model.addAttribute("message", "Thêm admin thành công!");
                }else {
                    model.addAttribute("message", "Không đủ hàng!");
                }
            }
            if (cartList1 != null) {
                cartList1.clear(); // Xóa tất cả sản phẩm khỏi giỏ hàng
                session.setAttribute("cartList1", cartList1); // Cập nhật lại session
                HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
                headerDTO.clear();
                session.setAttribute("headerDTO", headerDTO);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi thêm admin!");
        }

        return "redirect:/home/user/my-account/" + email;
    }


}
