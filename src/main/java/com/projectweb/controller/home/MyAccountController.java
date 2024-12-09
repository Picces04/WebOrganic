package com.projectweb.controller.home;

import com.projectweb.model.OgnBillingDetail;
import com.projectweb.model.OgnOrder;
import com.projectweb.model.OgnOrderdetail;
import com.projectweb.model.OgnUser;
import com.projectweb.model.dto.CartDTO;
import com.projectweb.model.dto.HeaderDTO;
import com.projectweb.model.dto.TotalOrderItemDTO;
import com.projectweb.service.admin.BillService;
import com.projectweb.service.admin.OrderDetailsService;
import com.projectweb.service.admin.OrderService;
import com.projectweb.service.admin.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home/user")
public class MyAccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BillService billService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/my-account/{email}")
    public String editdata(@PathVariable("email") String email, @RequestParam(value =  "s", required = false) Long s ,
                           HttpSession session, Model model) {
        OgnUser PickUser = userService.findByEmail(email);
        model.addAttribute("PickUser", PickUser);

        //Lấy session git
        OgnUser UserGitSS = (OgnUser) session.getAttribute("UserGitSS");
        model.addAttribute("UserGitSS", UserGitSS);

        OgnUser userPick = (OgnUser) session.getAttribute("userPick");
        if (userPick == null) {
            userPick = new OgnUser() {
            }; // Khởi tạo đối tượng mới
        }
        if (PickUser != null) {
            userPick.setUserid(PickUser.getUserid()); // Ví dụ với trường 'name'
            userPick.setEmail(PickUser.getEmail());
            userPick.setUsername(PickUser.getUsername());
            userPick.setPhone(PickUser.getPhone());
            // Thêm các trường khác tùy thuộc vào cấu trúc của OgnUser
        }

        session.setAttribute("userPick", userPick);
        model.addAttribute("userPick", userPick);

        //Lấy session cho giỏ hàng
        List<CartDTO> cartList1 = (List<CartDTO>) session.getAttribute("cartList");
        model.addAttribute("cartList1", cartList1);

        HeaderDTO headerDTO = (HeaderDTO) session.getAttribute("headerDTO");
        model.addAttribute("headerDTO", headerDTO);

        HeaderDTO totalWish = (HeaderDTO) session.getAttribute("totalWish");
        model.addAttribute("totalWish", totalWish);
        //Lấy session cho giỏ hàng

        //Lấy danh sách đặt hàng
        List<OgnOrder> dataOrder = orderService.findAll();
        model.addAttribute("dataOrder", dataOrder);

        List<OgnBillingDetail> dataBill = billService.findAll();
        model.addAttribute("dataBill", dataBill);


        List<OgnOrderdetail> dataDetail = orderDetailsService.findAll();
        model.addAttribute("dataDetail", dataDetail);

        if(s != null){
            OgnOrder order = orderService.findById(s);
            OgnBillingDetail dataBillPick = billService.findByOrderId(order);
            model.addAttribute("dataBillPick", dataBillPick);

            List<OgnOrderdetail>  PickDetail =  orderDetailsService.findById(order);
            model.addAttribute("PickDetail", PickDetail);
        }

        List<TotalOrderItemDTO> dataTotalItem = orderDetailsService.findTotal();
        model.addAttribute("dataTotalItem", dataTotalItem);

        return "/home/user/my-account";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @RequestParam("userid") String userId,
                                 Model model) {
        try {
            // Tìm người dùng
            OgnUser user = userService.findById(userId);

            // Kiểm tra mật khẩu hiện tại
            if (!passwordEncoder.matches(currentPassword, user.getPasswordhash())) {
                model.addAttribute("error", "Mật khẩu hiện tại không đúng!");
                return "redirect:/home/user/my-account"; // Tên file HTML form đổi mật khẩu
            }

            // Kiểm tra khớp mật khẩu mới và xác nhận
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
                return "redirect:/home/user/my-account";
            }

            // Cập nhật mật khẩu mới
            user.setPasswordhash(newPassword);
            userService.updateUser(user);

            model.addAttribute("message", "Đổi mật khẩu thành công!");
            return "redirect:/admin/security/ADauthentication-login"; // Trang thông báo thành công
        } catch (Exception ex) {
            model.addAttribute("error", "Đã xảy ra lỗi: " + ex.getMessage());
            return "redirect:/home/user/my-account";
        }
    }
}
