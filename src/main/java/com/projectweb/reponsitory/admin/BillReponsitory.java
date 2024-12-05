package com.projectweb.reponsitory.admin;


import com.projectweb.model.OgnBillingDetail;
import com.projectweb.model.OgnOrder;
import com.projectweb.model.OgnOrderdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BillReponsitory extends JpaRepository<OgnBillingDetail, String> , JpaSpecificationExecutor<OgnBillingDetail> {
    Optional<OgnBillingDetail> findFirstById(Long id);
    OgnBillingDetail findByOrderid(OgnOrder order);
}
