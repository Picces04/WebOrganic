package com.projectweb.reponsitory.admin;


import com.projectweb.model.OgnOrder;
import com.projectweb.model.OgnOrderdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsReponsitory extends JpaRepository<OgnOrderdetail, String> , JpaSpecificationExecutor<OgnOrderdetail> {
    Optional<OgnOrderdetail> findFirstById(Long id);

    List<OgnOrderdetail> findByOrderid(OgnOrder order);
}
