package com.projectweb.reponsitory.admin;


import com.projectweb.model.OgnBillingDetail;
import com.projectweb.model.OgnOrder;
import com.projectweb.model.OgnOrderdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BillReponsitory extends JpaRepository<OgnBillingDetail, String> , JpaSpecificationExecutor<OgnBillingDetail> {
    Optional<OgnBillingDetail> findFirstById(Long id);
    OgnBillingDetail findByOrderid(OgnOrder order);

    @Query("SELECT EXTRACT(MONTH FROM b.orderid.orderdate) AS month, " +
            "EXTRACT(YEAR FROM b.orderid.orderdate) AS year, " +
            "SUM(b.totalprice) AS totalRevenue " +
            "FROM OgnBillingDetail b " +
            "WHERE b.orderid.status = 'Hoàn thành' " +
            "GROUP BY EXTRACT(YEAR FROM b.orderid.orderdate), EXTRACT(MONTH FROM b.orderid.orderdate) " +
            "ORDER BY year, month")
    List<Object[]> findMonthlyRevenue();
}
