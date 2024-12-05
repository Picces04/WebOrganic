package com.projectweb.service.home;

import com.projectweb.model.OgnProduct;
import org.springframework.data.domain.Page;

public interface ShopCategoryService {
    Page<OgnProduct> findAll(Double minPrice, Double maxPrice, int pageNumber, long id, String sortBy);
}
