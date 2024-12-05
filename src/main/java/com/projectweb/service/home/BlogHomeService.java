package com.projectweb.service.home;

import com.projectweb.model.OgnBlog;
import com.projectweb.model.OgnProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogHomeService {
    Page<OgnBlog> findAll(int pageNumber, String search, String tag);
    OgnBlog findOne(Long id);

    List<OgnBlog> findAll();
}
