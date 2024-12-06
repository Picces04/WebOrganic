package com.projectweb.service.impl.admin;

import com.projectweb.model.OgnProduct;
import com.projectweb.model.OgnStarrating;
import com.projectweb.reponsitory.admin.ProductReponsitory;
import com.projectweb.reponsitory.admin.ReviewReponsitory;
import com.projectweb.service.admin.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewReponsitory reviewReponsitory;

    @Autowired
    ProductReponsitory productReponsitory;

    @Override
    public Page<OgnStarrating> findAll(String search, int pageNumber) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 20);
            Specification<OgnStarrating> specification = Specification.where(null);

            if (search != null && !search.trim().isEmpty()) {
                specification = specification
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("status"), "%" + search + "%"))
                        .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("review"), "%" + search + "%"));

                try {
                    Long id = Long.parseLong(search);
                    specification = specification
                            .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userid").get("userid"), id))
                            .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productid").get("id"), id))
                            .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id))
                            .or((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("rating"), id));
                } catch (NumberFormatException e) {
                    System.out.println("search không phải kiểu Long, bỏ qua tìm kiếm theo id");
                }
            }

            return reviewReponsitory.findAll(specification, pageable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty(); // Trả về Page trống nếu có lỗi
    }


    @Override
    public List<OgnStarrating> findAll() {
        try {
            return reviewReponsitory.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OgnStarrating> findStatus() {
        try {
            return reviewReponsitory.findStatus("Home");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<OgnProduct, Integer> getAverageStarRatings() {
        List<OgnStarrating> allReviews = reviewReponsitory.findAll(); // Lấy toàn bộ đánh giá

        // Nhóm đánh giá theo sản phẩm
        Map<OgnProduct, List<OgnStarrating>> reviewsByProduct = allReviews.stream()
                .collect(Collectors.groupingBy(OgnStarrating::getProductid));

        Map<OgnProduct, Integer> averageRatings = new HashMap<>();

        for (OgnProduct product : productReponsitory.findAll()) { // Giả sử có hàm lấy tất cả sản phẩm
            List<OgnStarrating> reviews = reviewsByProduct.getOrDefault(product, List.of());

            if (reviews.isEmpty()) {
                // Nếu không có đánh giá, mặc định là 5 sao
                averageRatings.put(product, 5);
            } else {
                // Tính tổng sao
                long sumStars = reviews.stream()
                        .mapToLong(OgnStarrating::getRating)
                        .sum();

                // Tính trung bình
                double avgStar = (double) sumStars / reviews.size();

                // Làm tròn và giới hạn trong khoảng [1, 5]
                int roundedStar = (int) Math.round(avgStar);
                roundedStar = Math.max(1, Math.min(roundedStar, 5));

                averageRatings.put(product, roundedStar);
            }
        }

        return averageRatings;
    }

    @Override
    public Integer Totalreview() {
        List<OgnStarrating> total = reviewReponsitory.findAll();
        return total.size();
    }


    @Override
    public OgnStarrating insertReview(OgnStarrating review) {
        try {
            review.setProductid(review.getProductid());
            review.setUserid(review.getUserid());
            review.setRating(review.getRating());
            review.setRatingdate(review.getRatingdate());
            review.setReview(review.getReview());
            review.setStatus(review.getStatus());
            return reviewReponsitory.save(review);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OgnStarrating updateReview(OgnStarrating review) {
        try {
            // Chuyển đổi String thành Long
            Long ID = review.getId();

            // Tìm kiếm người dùng theo userid dưới dạng Long
            OgnStarrating existingReview = reviewReponsitory.findFirstById(ID)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Cập nhật thông tin người dùng
            existingReview.setStatus(review.getStatus());
            // Lưu người dùng đã cập nhật
            return reviewReponsitory.save(existingReview);
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
    public boolean deleteReview(long id) {
        try {
            OgnStarrating ognStarrating = reviewReponsitory.findFirstById(id).orElse(null);
            reviewReponsitory.delete(ognStarrating);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
