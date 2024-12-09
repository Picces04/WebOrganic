package com.projectweb.service.impl.home;


import com.projectweb.model.OgnProduct;
import com.projectweb.model.OgnSaleprice;
import com.projectweb.reponsitory.home.ShopCategoryReponsitory;
import com.projectweb.reponsitory.home.ShopReponsitory;
import com.projectweb.service.home.ShopCategoryService;
import jakarta.persistence.criteria.Join;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@AllArgsConstructor
public class ShopCategoryServiceImpl implements ShopCategoryService {
    ShopCategoryReponsitory shopCategoryReponsitory ;
    ShopReponsitory shopReponsitory ;

    @Override
    public Page<OgnProduct> findAll(Double minPrice, Double maxPrice, int pageNumber, long id, String sortBy) {
        try {
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

            int pageSize = 16;
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

            // Tạo Specification
            Specification<OgnProduct> specification = Specification.where(null);
            if (Objects.nonNull(id)) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("categoryid").get("id"), id));
            }
            if (minPrice != null && maxPrice != null) {
                specification = specification.and((root, query, criteriaBuilder) -> {
                    // Join OgnSaleprice từ OgnProduct
                    Join<OgnProduct, OgnSaleprice> salepriceJoin = root.join("ognSaleprices"); // Thay "saleprices" bằng tên thực tế của thuộc tính mối quan hệ
                    // Sử dụng price * salepercentage để tính giá đã giảm
                    return criteriaBuilder.between(
                            criteriaBuilder.prod(root.get("price"), salepriceJoin.get("salepercentage")),
                            minPrice,
                            maxPrice
                    );
                });
            }

            // Áp dụng Specification và Pageable
            return shopCategoryReponsitory.findAll(specification, pageable);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Page.empty();
    }

}
