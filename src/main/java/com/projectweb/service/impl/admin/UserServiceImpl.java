package com.projectweb.service.impl.admin;

import com.projectweb.model.OgnUser;
import com.projectweb.reponsitory.admin.UserReponsitory;
import com.projectweb.service.admin.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    // Inject PasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserReponsitory userReponsitory ;

    @Override
    public Page<OgnUser> findAll(String search, int pageNumber) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 15);
            Specification<OgnUser> specification = Specification.where(null);

            if (Objects.nonNull(search)) {
                specification = specification.or(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("username"), "%" + search + "%")))
                        .or(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("email"), "%" + search + "%")))
                        .or(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("userid"), "%" + search + "%")))
                        .or(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("role"), "%" + search + "%")))
                        .or(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("passwordhash"), "%" + search + "%")));
            }
            Page<OgnUser> page =  userReponsitory.findAll(specification,pageable);
            return page;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OgnUser> findAll() {
        try {
            return userReponsitory.findAll();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer TotalUsers() {
        List<OgnUser> total = userReponsitory.findAll();
        return total.size();
    }

    @Override
    public OgnUser findByEmail(String email) throws UsernameNotFoundException {
        try {
            return userReponsitory.findFirstByEmail(email);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnUser findById(String userid) throws UsernameNotFoundException {
        Optional<OgnUser> optionalUser = userReponsitory.findFirstByUserid(userid);
        return optionalUser.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        OgnUser user = userReponsitory.findFirstByEmail(email);

        // Tạo GrantedAuthority từ vai trò trong cột ROLE
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        return new User(
                user.getEmail(),
                user.getPasswordhash(),
                Collections.singleton(authority) // Gán vai trò
        );
    }


    //CRUD USER
    @Override
    @Transactional
    public OgnUser insertUser(OgnUser user) {
        try {
            // Tạo UUID và chuyển đổi thành chuỗi
            String userIdString = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            user.setUserid(userIdString); // Đặt userid là chuỗi đã chuyển đổi
            // Mã hóa mật khẩu trước khi lưu

            String hashedPassword = passwordEncoder.encode(user.getPasswordhash());
            user.setPasswordhash(hashedPassword); // Set mật khẩu đã mã hóa vào đối tượng người dùng
            user.setRole(user.getRole());
            return userReponsitory.save(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnUser updateUser(OgnUser user) {
        try {
            // Chuyển đổi UUID thành String
            String userIdString = user.getUserid().toString().replace("-", "").toUpperCase();

            // Tìm kiếm người dùng theo userid dưới dạng String
            OgnUser existingUser = userReponsitory.findFirstByUserid(userIdString)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

            // Cập nhật thông tin người dùng
            existingUser.setUsername(user.getUsername());
            if (user.getPasswordhash() != null && user.getRole() != null) {
                existingUser.setPasswordhash(user.getPasswordhash());
                existingUser.setRole(user.getRole());
            }
            existingUser.setPhone(user.getPhone());

            // Kiểm tra nếu người dùng nhập mật khẩu mới
            if (user.getPasswordhash() != null && !user.getPasswordhash().isEmpty()) {
                // Mã hóa mật khẩu trước khi lưu
                String hashedPassword = passwordEncoder.encode(user.getPasswordhash());
                existingUser.setPasswordhash(hashedPassword);
            }

            // Lưu người dùng đã cập nhật
            return userReponsitory.save(existingUser);
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
    public boolean deleteUser(String userid) {
        try {
            OgnUser user = userReponsitory.findFirstByUserid(userid).orElse(null);
            userReponsitory.delete(user);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            OgnUser user = userReponsitory.findFirstByEmail(email);
            if (user != null) {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       return true;
    }


}
