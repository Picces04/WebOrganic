package com.projectweb.service.admin;

import com.projectweb.model.OgnOrder;
import com.projectweb.model.OgnProduct;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface OrderService {
    Page<OgnOrder> findAll(int pageNumber, String search);

    List<OgnOrder> findAll();

    OgnOrder findById(long id) throws UsernameNotFoundException;

    List SumYear();

    OgnOrder insertOrder(OgnOrder order);

    OgnOrder updateOrder(OgnOrder order);

    boolean deleteOrder(long id);
}
