package com.projectweb.service.admin;

import com.projectweb.model.OgnOrder;
import com.projectweb.model.OgnOrderdetail;
import com.projectweb.model.dto.TotalOrderItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface OrderDetailsService {
    Page<OgnOrderdetail> findAll(int pageNumber, String search);

    List<OgnOrderdetail> findAll();

    List<TotalOrderItemDTO> findTotal();

    List<OgnOrderdetail>  findById(OgnOrder order) throws UsernameNotFoundException;

    OgnOrderdetail insertOrderdetail(OgnOrderdetail order);

    OgnOrderdetail updateOrderdetail(OgnOrderdetail order);

    boolean deleteOrderdetail(long id);
}
