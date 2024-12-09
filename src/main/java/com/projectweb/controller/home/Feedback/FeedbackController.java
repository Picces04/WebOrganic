package com.projectweb.controller.home.Feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send-feedback")
    public String sendFeedback(
            @RequestParam("con_name") String name,
            @RequestParam("con_email") String email,
            @RequestParam("con_subject") String subject,
            @RequestParam("con_message") String message,
            Model model) {

        try {
            // Tạo email phản hồi
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("khanhhaivodoi@gmail.com"); // Email server trung gian
            mailMessage.setTo("picces143@gmail.com"); // Email admin nhận phản hồi
            mailMessage.setSubject(subject.isEmpty() ? "Phản hồi từ khách hàng" : subject);
            mailMessage.setText(
                    "Tên: " + name + "\n" +
                            "Email: " + email + "\n\n" +
                            "Tin nhắn:\n" + message
            );
            mailMessage.setReplyTo(email); // Đặt email khách hàng làm "Reply-To"

            // Gửi email
            mailSender.send(mailMessage);

            // Thêm thông báo thành công vào model để hiển thị trên trang
            model.addAttribute("successMessage", "Phản hồi của bạn đã được gửi thành công!");
        } catch (Exception e) {
            // Thêm thông báo lỗi vào model nếu có lỗi xảy ra khi gửi email
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi gửi phản hồi. Vui lòng thử lại.");
            return "Thất bại !";
        }

        // Trả về tên view (trang hiện tại) mà không chuyển hướng đi đâu
        return "Thành công !";  // Thay đổi tên view nếu cần
    }
}
