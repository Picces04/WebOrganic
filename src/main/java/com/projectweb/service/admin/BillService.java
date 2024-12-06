package com.projectweb.service.admin;

import com.projectweb.model.OgnBillingDetail;
import com.projectweb.model.OgnOrder;
import com.projectweb.model.dto.RevenueDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface BillService {
    Page<OgnBillingDetail> findAll(int pageNumber, String search);

    List<OgnBillingDetail> findAll();

    OgnBillingDetail findByOrderId(OgnOrder order);

    OgnBillingDetail findById(long id) throws UsernameNotFoundException;

    OgnBillingDetail insertBill(OgnBillingDetail order);

    OgnBillingDetail updateBill(OgnBillingDetail order);

    boolean deleteBill(long id);

    List<RevenueDto> getMonthlyRevenue();
}
