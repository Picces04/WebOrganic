package com.projectweb.reponsitory.home;


import com.projectweb.model.OgnProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopReponsitory extends JpaRepository<OgnProduct, String> , JpaSpecificationExecutor<OgnProduct> {

    // Phương thức tìm kiếm sản phẩm trong khoảng giá sử dụng Specification
    Page<OgnProduct> findAll(Specification<OgnProduct> specification, Pageable pageable);

}
