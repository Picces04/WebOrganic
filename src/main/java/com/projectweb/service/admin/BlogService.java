package com.projectweb.service.admin;

import com.projectweb.model.OgnBlog;
import org.springframework.data.domain.Page;

public interface BlogService {
    Page<OgnBlog> findAll(String search, int pageNumber);

    Integer TotalBlogs();
    //CRUD Blog
    OgnBlog insertBlog(OgnBlog blog);
    OgnBlog updateBlog(OgnBlog blog);
    boolean deleteBlog(long id);
    //CRUD Blog
}
