package com.projectweb.service.admin;

import com.projectweb.model.OgnSaleprice;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SaleService {
    Page<OgnSaleprice> findAll(String search , int pageNumber);
    List<OgnSaleprice> findAll();
    List<OgnSaleprice> findSaleTheDay();

    //CRUD Admin
    OgnSaleprice insertSale(OgnSaleprice saleprice);
    OgnSaleprice updateSale(OgnSaleprice saleprice);
    boolean deleteSale(long id);
    //CRUD Admin
}
