package com.projectweb.service.admin;

import com.projectweb.model.OgnProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ProductService {
    Page<OgnProduct> findAll(String search , int pageNumber, Sort sort);
    List<OgnProduct> findAll();

    OgnProduct findOne(Long id);
    Integer Totalproduct();
    //CRUD Admin
    OgnProduct insertProduct(OgnProduct product);
    OgnProduct updateProduct(OgnProduct product);
    boolean deleteProduct(long id);
    //CRUD Admin
}
