package com.projectweb.service.impl.admin;

import com.projectweb.model.OgnSlide;
import com.projectweb.reponsitory.admin.SlideReponsitory;
import com.projectweb.service.admin.SlideService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SlideServiceImpl implements SlideService {
    SlideReponsitory slideReponsitory;

    @Override
    public List<OgnSlide> findAll() {
        try {
            return slideReponsitory.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OgnSlide> findStatusTop() {
        try {
            return slideReponsitory.findStatus("Top");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OgnSlide> findStatusMid() {
        try {
            return slideReponsitory.findStatus("Mid");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OgnSlide> findStatusBottom() {
        try {
            return slideReponsitory.findStatus("Bottom");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnSlide insertSlide(OgnSlide slide) {
        try {
            slide.setDescr(slide.getDescr().trim());
            slide.setStatus(slide.getStatus().trim());
            return slideReponsitory.save(slide);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnSlide updateSlide(OgnSlide slide) {
        try {
            // Chuyển đổi String thành Long
            Long Id = slide.getId();

            // Tìm kiếm người dùng theo userid dưới dạng Long
            OgnSlide existingSlide = slideReponsitory.findFirstById(Id)
                    .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

            // Cập nhật thông tin người dùng
            existingSlide.setImageurl(slide.getImageurl());
            existingSlide.setDescr(slide.getDescr());
            existingSlide.setStatus(slide.getStatus().trim());

            // Lưu người dùng đã cập nhật
            return slideReponsitory.save(existingSlide);
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
    public boolean deleteSlide(long id) {
        try {
            OgnSlide ognSlide = slideReponsitory.findFirstById(id).orElse(null);
            slideReponsitory.delete(ognSlide);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
