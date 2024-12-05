package com.projectweb.reponsitory.admin;


import com.projectweb.model.OgnOrder;
import com.projectweb.model.OgnProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrderReponsitory extends JpaRepository<OgnOrder, String> , JpaSpecificationExecutor<OgnOrder> {
    Optional<OgnOrder> findFirstById(Long id);
}
