package com.projectweb.service.impl.admin;

import com.projectweb.model.OgnAdmin;
import com.projectweb.model.OgnUser;
import com.projectweb.reponsitory.admin.AdminReponsitory;
import com.projectweb.reponsitory.admin.UserReponsitory;
import com.projectweb.service.admin.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminSerivceImpl implements AdminService {

    @Autowired
    private AdminReponsitory adminReponsitory ;

    @Autowired
    private UserReponsitory userReponsitory ;

    @Override
    public Page<OgnAdmin> findAll(String search, int pageNumber) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 10);
            Specification<OgnAdmin> specification = Specification.where(null);

            if (Objects.nonNull(search)) {
                specification = specification
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("role"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("userid").get("userid"), "%" + search + "%"));


                // Thêm điều kiện tìm kiếm theo id
                try {
                    Long id = Long.parseLong(search);  // Chuyển search thành Long
                    specification = specification
                            .or( (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
                } catch (NumberFormatException e) {
                    // Nếu không thể chuyển đổi search thành Long, thì bỏ qua phần này
                    System.out.println("search không phải kiểu Long, bỏ qua tìm kiếm theo id");
                }
            }

            Page<OgnAdmin> page = adminReponsitory.findAll(specification, pageable);
            return page != null ? page : Page.empty(pageable); // Trả về Page trống nếu không có dữ liệu
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty(); // Trả về Page trống nếu có lỗi
    }

    @Override
    public OgnAdmin findById(long id) throws UsernameNotFoundException {
        Optional<OgnAdmin> optionalOgnAdmin = adminReponsitory.findById(String.valueOf(id));
        return optionalOgnAdmin.orElse(null);
    }

    @Override
    public List<OgnAdmin> findFirstByUserid(String userid) {
        return List.of();
    }

    @Override
    @Transactional
    public OgnAdmin insertAdmin(OgnAdmin admin) {
        try {
            admin.setUserid(admin.getUserid());
            admin.setRole(admin.getRole());
            return adminReponsitory.save(admin);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnAdmin updateAdmin(OgnAdmin admin) {
        try {
            // Chuyển đổi String thành Long
            Long userIdString = admin.getId();

            // Tìm kiếm người dùng theo userid dưới dạng Long
            OgnAdmin existingAdmin = adminReponsitory.findFirstById(userIdString)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

            // Cập nhật thông tin người dùng
            existingAdmin.setUserid(admin.getUserid());
            existingAdmin.setRole(admin.getRole());

            // Lưu người dùng đã cập nhật
            return adminReponsitory.save(existingAdmin);
        } catch (RuntimeException ex) {
            // Xử lý riêng cho RuntimeException
            System.err.println(ex.getMessage()); // In ra thông báo lỗi
            throw ex; // Ném lại lỗi để xử lý tiếp
        } catch (Exception ex) {
            // Xử lý cho các trường hợp ngoại lệ khác
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean deleteAdmin(long id) {
        try {
            OgnAdmin ognAdmin = adminReponsitory.findFirstById(id).orElse(null);
            adminReponsitory.delete(ognAdmin);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean validatePermission(String... permission) {
        // Lấy thông tin người dùng đã đăng nhập từ SecurityContextHolder
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Kiểm tra nếu đối tượng ognUser là null
        if (user == null) {
            return false;  // Trả về false nếu không có thông tin người dùng
        }
        OgnUser ognUser = userReponsitory.findFirstByEmail(user.getUsername());

        // Truy vấn OgnAdmin để lấy thông tin role của người dùng từ OgnAdmin
        List<OgnAdmin> roles = adminReponsitory.findByUserid(ognUser);

        if (roles.isEmpty()) {
            return false;  // Trả về false nếu không tìm thấy roles
        }

        // Kiểm tra quyền của người dùng
        for (String role : permission) {
            // Kiểm tra xem role trong `roles` có khớp với quyền yêu cầu không
            if (roles.stream().anyMatch(r -> r.getRole().equals(role))) {
                return true;  // Trả về true nếu có quyền tương ứng
            }
        }

        return false;  // Trả về false nếu không có quyền tương ứng
    }





}
