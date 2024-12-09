package com.projectweb.service.impl.admin;

import com.projectweb.model.OgnCategory;
import com.projectweb.reponsitory.admin.CategoryReponsitory;
import com.projectweb.service.admin.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryReponsitory categoryReponsitory;

    @Override
    public List<OgnCategory> findAll() {
        try {
            return categoryReponsitory.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OgnCategory> findStatus() {
        try {
            return categoryReponsitory.findStatus("Home");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnCategory findPickCategory(long id) {
        Optional<OgnCategory> category = categoryReponsitory.findFirstById(id);
        return category.orElse(null); // Trả về null nếu không tìm thấy sản phẩm
    }

    @Override
    public OgnCategory insertCategory(OgnCategory category) {
        try {
            category.setCategoryname(category.getCategoryname());
            category.setCategoryimages(category.getCategoryimages());
            category.setStatus(category.getStatus());
            return categoryReponsitory.save(category);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnCategory updateCategory(OgnCategory category) {
        try {
            // Chuyển đổi String thành Long
            Long categoryId = category.getId();

            // Tìm kiếm người dùng theo userid dưới dạng Long
            OgnCategory existingCategory = categoryReponsitory.findFirstById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

            // Cập nhật thông tin người dùng
            existingCategory.setCategoryname(category.getCategoryname());
            existingCategory.setCategoryimages(category.getCategoryimages());
            existingCategory.setStatus(category.getStatus());

            // Lưu người dùng đã cập nhật
            return categoryReponsitory.save(existingCategory);
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
    public boolean deleteCategory(long id) {
        try {
            OgnCategory ognCategory = categoryReponsitory.findFirstById(id).orElse(null);
            categoryReponsitory.delete(ognCategory);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
