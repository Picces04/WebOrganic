package com.projectweb.reponsitory.admin;

import com.projectweb.model.OgnProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface ProductReponsitory extends JpaRepository<OgnProduct, String> , JpaSpecificationExecutor<OgnProduct> {
    Optional<OgnProduct> findFirstById(Long id);

    Optional<OgnProduct> findById(Long id);

}
