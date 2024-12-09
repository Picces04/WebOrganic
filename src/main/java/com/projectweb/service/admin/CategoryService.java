package com.projectweb.service.admin;

import com.projectweb.model.OgnCategory;

import java.util.List;

public interface CategoryService {
    List<OgnCategory> findAll();
    List<OgnCategory> findStatus();

    OgnCategory findPickCategory(long id);

    //CRUD Admin
    OgnCategory insertCategory(OgnCategory category);
    OgnCategory updateCategory(OgnCategory category);
    boolean deleteCategory(long id);
    //CRUD Admin
}
