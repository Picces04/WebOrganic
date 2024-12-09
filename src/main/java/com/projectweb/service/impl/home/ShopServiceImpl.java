package com.projectweb.service.impl.home;


import com.projectweb.model.OgnProduct;
import com.projectweb.model.OgnSaleprice;
import com.projectweb.reponsitory.home.ShopReponsitory;
import com.projectweb.service.home.ShopService;
import jakarta.persistence.criteria.Join;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;


@Service
@AllArgsConstructor
public class ShopServiceImpl implements ShopService {
    ShopReponsitory shopReponsitory;

    @Override
    public Page<OgnProduct> findAll(Double minPrice, Double maxPrice, int pageNumber, String sortBy, String search) {
        try {
            // Xác định điều kiện sắp xếp
            Sort sort;
            switch (sortBy) {
                case "AtoZ":
                    sort = Sort.by(Sort.Direction.ASC, "productname"); // Sắp xếp tên từ A-Z
                    break;
                case "ZtoA":
                    sort = Sort.by(Sort.Direction.DESC, "productname"); // Sắp xếp tên từ Z-A
                    break;
                case "asc":
                    sort = Sort.by(Sort.Direction.ASC, "price"); // Giá tăng dần
                    break;
                case "desc":
                    sort = Sort.by(Sort.Direction.DESC, "price"); // Giá giảm dần
                    break;
                default:
                    sort = Sort.unsorted(); // Không sắp xếp
                    break;
            }

            // Tạo Pageable với số lượng sản phẩm mỗi trang và thứ tự sắp xếp
            int pageSize = 16; // Số lượng sản phẩm mỗi trang
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

            // Khởi tạo Specification ban đầu là null
            Specification<OgnProduct> specification = Specification.where(null);

            // Thêm điều kiện tìm kiếm theo tên sản phẩm nếu có
            if (Objects.nonNull(search)) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        // Sử dụng lower() để chuyển cả productname và từ khóa tìm kiếm thành chữ thường
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("productname")),
                                "%" + search.toLowerCase() + "%"
                        )
                );
            }


            // Lọc theo giá và giảm giá
            if (minPrice != null && maxPrice != null) {
                specification = specification.and((root, query, criteriaBuilder) -> {
                    // Join với bảng OgnSaleprice để lấy salepercentage
                    Join<OgnProduct, OgnSaleprice> salepriceJoin = root.join("ognSaleprices");

                    // Lọc theo giá: price * salepercentage nằm trong khoảng minPrice và maxPrice
                    return criteriaBuilder.between(
                            criteriaBuilder.prod(root.get("price"), salepriceJoin.get("salepercentage")), minPrice, maxPrice
                    );
                });
            }

            // Trả về kết quả với Specification và Pageable
            return shopReponsitory.findAll(specification, pageable);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty();
    }


}
