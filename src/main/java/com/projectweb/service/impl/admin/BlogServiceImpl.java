package com.projectweb.service.impl.admin;


import com.projectweb.model.OgnBlog;
import com.projectweb.reponsitory.admin.BlogReponsitory;
import com.projectweb.service.admin.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@AllArgsConstructor
public class BlogServiceImpl implements BlogService {
    BlogReponsitory blogReponsitory;

    @Override
    public Page<OgnBlog> findAll(String search, int pageNumber) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 20);
            Specification<OgnBlog> specification = Specification.where(null);

            if (search != null && !search.trim().isEmpty()) {
                specification = specification
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("blogname"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("blogdesc"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("tag"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("blogshortdesc"), "%" + search + "%"));

                try {
                    Long id = Long.parseLong(search);
                    specification = specification
                            .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
                } catch (NumberFormatException e) {
                    System.out.println("search không phải kiểu Long, bỏ qua tìm kiếm theo id");
                }
                try {
                    LocalDate date = LocalDate.parse(search);
                    specification = specification
                            .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("blogdate"), date));
                } catch (NumberFormatException e) {
                    System.out.println("search không phải kiểu Date, bỏ qua tìm kiếm theo date");
                }
            }

            return blogReponsitory.findAll(specification, pageable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty(); // Trả về Page trống nếu có lỗi
    }

    @Override
    public Integer TotalBlogs() {
        List<OgnBlog> blogs = blogReponsitory.findAll();
        return blogs.size();
    }

    @Override
    public OgnBlog insertBlog(OgnBlog blog) {
        try {
            blog.setBlogname(blog.getBlogname());
            blog.setTag(blog.getTag());
            blog.setBlogdesc(blog.getBlogdesc());
            blog.setBlogshortdesc(blog.getBlogshortdesc());
            return blogReponsitory.save(blog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnBlog updateBlog(OgnBlog blog) {
        try {
            // Chuyển đổi String thành Long
            Long ID = blog.getId();

            // Tìm kiếm người dùng theo id dưới dạng Long
            OgnBlog blog1 = blogReponsitory.findFirstById(ID)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Cập nhật thông tin người dùng
            blog1.setImage(blog.getImage());
            blog1.setBlogname(blog.getBlogname());
            blog1.setTag(blog.getTag());
            blog1.setBlogdesc(blog.getBlogdesc());
            blog1.setBlogshortdesc(blog.getBlogshortdesc());

            // Lưu người dùng đã cập nhật
            return blogReponsitory.save(blog1);
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
    public boolean deleteBlog(long id) {
        try {
            OgnBlog blog = blogReponsitory.findFirstById(id).orElse(null);
            blogReponsitory.delete(blog);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


}
