package com.projectweb.service.home;

import com.projectweb.model.OgnProduct;
import org.springframework.data.domain.Page;

public interface ShopService {

    Page<OgnProduct> findAll(Double minPrice, Double maxPrice, int pageNumber, String sortBy, String search);
}
